package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.exception.ErrorType.CONTEXT
import com.procurement.budget.exception.ErrorType.EI_NOT_FOUND
import com.procurement.budget.exception.ErrorType.FS_NOT_FOUND
import com.procurement.budget.exception.ErrorType.INVALID_CURRENCY
import com.procurement.budget.exception.ErrorType.INVALID_EUROPEAN
import com.procurement.budget.exception.ErrorType.INVALID_OCID
import com.procurement.budget.exception.ErrorType.INVALID_OWNER
import com.procurement.budget.exception.ErrorType.INVALID_PERIOD
import com.procurement.budget.exception.ErrorType.INVALID_STATUS
import com.procurement.budget.lib.errorIfBlank
import com.procurement.budget.model.dto.bpe.CommandMessage
import com.procurement.budget.model.dto.bpe.ResponseDto
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.ei.ValueEi
import com.procurement.budget.model.dto.fs.BudgetFs
import com.procurement.budget.model.dto.fs.Fs
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.fs.PlanningFs
import com.procurement.budget.model.dto.fs.SourceEntityFs
import com.procurement.budget.model.dto.fs.TenderFs
import com.procurement.budget.model.dto.fs.request.FsCreate
import com.procurement.budget.model.dto.fs.request.FsUpdate
import com.procurement.budget.model.dto.fs.response.EiForFs
import com.procurement.budget.model.dto.fs.response.EiForFsBudget
import com.procurement.budget.model.dto.fs.response.EiForFsPlanning
import com.procurement.budget.model.dto.fs.response.FsResponse
import com.procurement.budget.model.dto.ocds.Identifier
import com.procurement.budget.model.dto.ocds.Period
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.FsEntity
import com.procurement.budget.utils.getDuplicate
import com.procurement.budget.utils.localNowUTC
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toLocal
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class FsService(private val fsDao: FsDao,
                private val eiDao: EiDao,
                private val generationService: GenerationService) {

    fun createFs(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val fsDto = toObject(FsCreate::class.java, cm.data)
        fsDto.validateTextAttributes()
        fsDto.validateDuplicates()

        validatePeriod(fsDto.planning.budget.period)

        if (fsDto.planning.budget.isEuropeanUnionFunded && fsDto.planning.budget.europeanUnionFunding == null) {
            throw ErrorException(INVALID_EUROPEAN)
        }
        if (!fsDto.planning.budget.isEuropeanUnionFunded) {
            fsDto.planning.budget.europeanUnionFunding = null
        }
        if (fsDto.planning.budget.amount.amount <= BigDecimal.valueOf(0.00)) {
            throw ErrorException(ErrorType.INVALID_AMOUNT)
        }

        val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
        val ei = toObject(Ei::class.java, eiEntity.jsonData)
        checkPeriodWithEi(ei.planning.budget.period, fsDto.planning.budget.period)
        checkCurrency(ei, fsDto.planning.budget.amount.currency)

        val tenderStatusFs: TenderStatus
        val funderFs: OrganizationReferenceFs?
        val sourceEntityFs: SourceEntityFs
        val verifiedFs: Boolean
        if (fsDto.buyer != null) {
            funderFs = fsDto.buyer
            funderFs.apply { id = identifier.scheme + SEPARATOR + identifier.id }
            sourceEntityFs = getSourceEntity(funderFs)
            verifiedFs = true
            tenderStatusFs = TenderStatus.ACTIVE
        } else {
            funderFs = null
            sourceEntityFs = getSourceEntity(getFounderFromEi(ei.buyer))
            verifiedFs = false
            tenderStatusFs = TenderStatus.PLANNING
        }
        val ocid = getOcId(cpId)
        val budgetDto = fsDto.planning.budget
        val fs = Fs(
                ocid = ocid,
                token = null,
                tender = TenderFs(
                        id = generationService.generateTenderId().toString(),
                        status = tenderStatusFs,
                        statusDetails = TenderStatusDetails.EMPTY,
                        procuringEntity = null),
                planning = PlanningFs(
                        budget = BudgetFs(
                                id = budgetDto.id,
                                description = budgetDto.description,
                                period = budgetDto.period,
                                amount = budgetDto.amount,
                                isEuropeanUnionFunded = budgetDto.isEuropeanUnionFunded,
                                europeanUnionFunding = budgetDto.europeanUnionFunding,
                                verificationDetails = null,
                                sourceEntity = sourceEntityFs,
                                verified = verifiedFs,
                                project = budgetDto.project,
                                projectID = budgetDto.projectID,
                                uri = budgetDto.uri
                        ),
                        rationale = fsDto.planning.rationale
                ),
                funder = funderFs,
                payer = fsDto.tender.procuringEntity.apply { id = identifier.scheme + SEPARATOR + identifier.id }
        )
        val fsEntity = getEntity(cpId, fs, owner, dateTime)
        fsDao.save(fsEntity)
        fs.token = fsEntity.token.toString()
        //ei
        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
        ei.planning.budget.amount = ValueEi(
                amount = totalAmount,
                currency = fs.planning.budget.amount.currency)
        eiEntity.jsonData = toJson(ei)
        eiDao.save(eiEntity)
        return ResponseDto(data = FsResponse(getEiForFs(ei), fs))
    }

    fun updateFs(cm: CommandMessage): ResponseDto {

        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val ocId = cm.context.ocid ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val fsDto = toObject(FsUpdate::class.java, cm.data)
        fsDto.validateTextAttributes()

        validatePeriod(fsDto.planning.budget.period)
        if (fsDto.planning.budget.isEuropeanUnionFunded && fsDto.planning.budget.europeanUnionFunding == null) {
            throw ErrorException(INVALID_EUROPEAN)
        }
        if (!fsDto.planning.budget.isEuropeanUnionFunded) {
            fsDto.planning.budget.europeanUnionFunding = null
        }
        if (fsDto.planning.budget.amount.amount < BigDecimal.valueOf(0.00)) {
            throw ErrorException(ErrorType.INVALID_JSON_TYPE)
        }

        val fsEntity = fsDao.getByCpIdAndToken(cpId, UUID.fromString(token))
                ?: throw ErrorException(FS_NOT_FOUND)
        if (fsEntity.ocId != ocId) throw ErrorException(INVALID_OCID)
        if (fsEntity.owner != owner) throw ErrorException(INVALID_OWNER)
        val fs = toObject(Fs::class.java, fsEntity.jsonData)
        val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
        val ei = toObject(Ei::class.java, eiEntity.jsonData)
        checkPeriodWithEi(ei.planning.budget.period, fsDto.planning.budget.period)
        checkCurrency(ei, fsDto.planning.budget.amount.currency)
        if (fs.tender.statusDetails != TenderStatusDetails.EMPTY) throw ErrorException(INVALID_STATUS)
        when (fs.tender.status) {
            TenderStatus.ACTIVE -> fsUpdate(fs, fsDto)
            TenderStatus.PLANNING -> {
                fsUpdate(fs, fsDto)
                fs.planning.budget.id = fsDto.planning.budget.id
            }
            else -> throw ErrorException(INVALID_STATUS)
        }
        fsEntity.jsonData = toJson(fs)
        fsEntity.amount = fs.planning.budget.amount.amount
        fsDao.save(fsEntity)
        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
        var eiForFs: EiForFs? = null
        if (totalAmount != ei.planning.budget.amount?.amount) {
            ei.planning.budget.amount?.amount = totalAmount
            eiEntity.jsonData = toJson(ei)
            eiDao.save(eiEntity)
            eiForFs = getEiForFs(ei)
        }
        return ResponseDto(data = FsResponse(eiForFs, fs))
    }

    private fun FsCreate.validateTextAttributes() {
        buyer?.identifier?.id.checkForBlank("buyer.identifier.id")
        buyer?.identifier?.legalName.checkForBlank("buyer.identifier.legalName")
        buyer?.identifier?.scheme.checkForBlank("buyer.identifier.scheme")
        buyer?.identifier?.uri.checkForBlank("buyer.identifier.uri")
        buyer?.name.checkForBlank("buyer.name")
        planning.budget.description.checkForBlank("planning.budget.description")
        planning.budget.europeanUnionFunding?.projectIdentifier.checkForBlank("planning.budget.europeanUnionFunding.projectIdentifier")
        planning.budget.europeanUnionFunding?.projectName.checkForBlank("planning.budget.europeanUnionFunding.projectName")
        planning.budget.europeanUnionFunding?.uri.checkForBlank("planning.budget.europeanUnionFunding.uri")
        planning.budget.id.checkForBlank("planning.budget.id")
        planning.budget.project.checkForBlank("planning.budget.project")
        planning.budget.projectID.checkForBlank("planning.budget.projectID")
        planning.budget.uri.checkForBlank("planning.budget.uri")
        planning.rationale.checkForBlank("planning.rationale")
        planning.budget.id.checkForBlank("planning.budget.id")
        buyer?.address?.streetAddress.checkForBlank("buyer.address.streetAddress")
        buyer?.address?.postalCode.checkForBlank("buyer.address.postalCode")
        buyer?.address?.addressDetails?.locality?.scheme.checkForBlank("buyer.address.addressDetails.locality.scheme")
        buyer?.address?.addressDetails?.locality?.id.checkForBlank("buyer.address.addressDetails.locality.id")
        buyer?.address?.addressDetails?.locality?.description.checkForBlank("buyer.address.addressDetails.locality.description")
        buyer?.additionalIdentifiers?.mapIndexed { i, identifier ->
            identifier.id.checkForBlank("buyer.additionalIdentifiers.[$i]id")
            identifier.scheme.checkForBlank("buyer.additionalIdentifiers.[$i]scheme")
            identifier.legalName.checkForBlank("buyer.additionalIdentifiers.[$i]legalName")
            identifier.uri.checkForBlank("buyer.additionalIdentifiers.[$i]uri")
        }
        buyer?.contactPoint?.name.checkForBlank("buyer.contactPoint.name")
        buyer?.contactPoint?.email.checkForBlank("buyer.contactPoint.email")
        buyer?.contactPoint?.telephone.checkForBlank("buyer.contactPoint.telephone")
        buyer?.contactPoint?.faxNumber.checkForBlank("buyer.contactPoint.faxNumber")
        buyer?.contactPoint?.url.checkForBlank("buyer.contactPoint.url")
        tender.procuringEntity.name.checkForBlank("tender.procuringEntity.name")
        tender.procuringEntity.identifier.id.checkForBlank("tender.procuringEntity.identifier.id")
        tender.procuringEntity.identifier.legalName.checkForBlank("tender.procuringEntity.identifier.legalName")
        tender.procuringEntity.identifier.uri.checkForBlank("tender.procuringEntity.identifier.uri")
        tender.procuringEntity.additionalIdentifiers?.mapIndexed { i, identifier ->
            identifier.id.checkForBlank("tender.procuringEntity.additionalIdentifiers.[$i]id")
            identifier.scheme.checkForBlank("tender.procuringEntity.additionalIdentifiers.[$i]scheme")
            identifier.legalName.checkForBlank("tender.procuringEntity.additionalIdentifiers.[$i]legalName")
            identifier.uri.checkForBlank("tender.procuringEntity.additionalIdentifiers.[$i]uri")
        }
        tender.procuringEntity.address.streetAddress.checkForBlank("tender.procuringEntity.address.streetAddress")
        tender.procuringEntity.address.postalCode.checkForBlank("tender.procuringEntity.address.postalCode")
        tender.procuringEntity.address.addressDetails.locality.scheme.checkForBlank("tender.procuringEntity.address.addressDetails.locality.scheme")
        tender.procuringEntity.address.addressDetails.locality.id.checkForBlank("tender.procuringEntity.address.addressDetails.locality.id")
        tender.procuringEntity.address.addressDetails.locality.description.checkForBlank("tender.procuringEntity.address.addressDetails.locality.description")
        tender.procuringEntity.contactPoint.name.checkForBlank("tender.procuringEntity.contactPoint.name")
        tender.procuringEntity.contactPoint.email.checkForBlank("tender.procuringEntity.contactPoint.email")
        tender.procuringEntity.contactPoint.telephone.checkForBlank("tender.procuringEntity.contactPoint.telephone")
        tender.procuringEntity.contactPoint.faxNumber.checkForBlank("tender.procuringEntity.contactPoint.faxNumber")
        tender.procuringEntity.contactPoint.url.checkForBlank("tender.procuringEntity.contactPoint.url")
    }

    private fun FsUpdate.validateTextAttributes() {
        planning.apply{
            rationale.checkForBlank("planning.rationale")

            budget.apply {
                id.checkForBlank("planning.budget.id")
                description.checkForBlank("planning.budget.description")
                project.checkForBlank("planning.budget.project")
                projectID.checkForBlank("planning.budget.projectID")
                uri.checkForBlank("planning.budget.uri")

                europeanUnionFunding?.apply {
                    projectIdentifier.checkForBlank("planning.budget.europeanUnionFunding.projectIdentifier")
                    projectName.checkForBlank("planning.budget.europeanUnionFunding.projectName")
                    uri.checkForBlank("planning.budget.europeanUnionFunding.uri")
                }
            }
        }
    }

    private fun String?.checkForBlank(name: String) = this.errorIfBlank {
        ErrorException(
            error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
            message = "The attribute '$name' is empty or blank."
        )
    }

    private fun FsCreate.validateDuplicates() {
        tender.procuringEntity.additionalIdentifiers.checkIdentifiersForDuplicates("tender.procuringEntity.additionalIdentifiers")
        buyer?.additionalIdentifiers.checkIdentifiersForDuplicates("buyer.additionalIdentifiers")
    }

    private fun List<Identifier>?.checkIdentifiersForDuplicates(attributeName: String) {
        val duplicateIdentifier = this?.getDuplicate { it.scheme.toUpperCase() + it.id.toUpperCase() }

        if (duplicateIdentifier != null)
            throw ErrorException(
                error = ErrorType.DUPLICATE,
                message = "Attribute '$attributeName' has duplicate by scheme '${duplicateIdentifier.scheme}' and id '${duplicateIdentifier.id}'."
            )
    }

    private fun fsUpdate(fs: Fs, fsUpdate: FsUpdate) {

        fs.planning.apply {
            rationale = fsUpdate.planning.rationale
            budget.apply {
                period = fsUpdate.planning.budget.period
                description = fsUpdate.planning.budget.description
                amount.amount = fsUpdate.planning.budget.amount.amount
                project = fsUpdate.planning.budget.project
                projectID = fsUpdate.planning.budget.projectID
                uri = fsUpdate.planning.budget.uri
                isEuropeanUnionFunded = fsUpdate.planning.budget.isEuropeanUnionFunded
                europeanUnionFunding = fsUpdate.planning.budget.europeanUnionFunding
            }
        }
    }

    private fun validatePeriod(period: Period) {
        if (period.startDate >= period.endDate) throw ErrorException(INVALID_PERIOD)
        if (period.endDate <= localNowUTC()) throw ErrorException(INVALID_PERIOD)
    }

    private fun checkPeriodWithEi(eiPeriod: Period, fsPeriod: Period) {
        val (eiStartDate, eiEndDate) = eiPeriod
        val (fsStartDate, fsEndDate) = fsPeriod
        val fsPeriodValid = (fsStartDate >= eiStartDate) && (fsEndDate <= eiEndDate)
        if (!fsPeriodValid) throw ErrorException(INVALID_PERIOD)
    }

    private fun checkCurrency(ei: Ei, fsCurrency: String) {
        val eiCurrency = ei.planning.budget.amount?.currency
        if (eiCurrency != null) {
            if (eiCurrency != fsCurrency) throw ErrorException(INVALID_CURRENCY)
        }
    }

    private fun getFounderFromEi(buyer: OrganizationReferenceEi): OrganizationReferenceFs {
        return OrganizationReferenceFs(
            id = buyer.id,
            name = buyer.name,
            identifier = buyer.identifier,
            address = buyer.address,
            additionalIdentifiers = buyer.additionalIdentifiers ?: emptyList(),
            contactPoint = buyer.contactPoint
        )
    }

    private fun getSourceEntity(funder: OrganizationReferenceFs): SourceEntityFs {
        return SourceEntityFs(
                id = funder.id!!,
                name = funder.name)
    }

    private fun getOcId(cpId: String): String {
        return cpId + FS_SEPARATOR + generationService.getNowUtc()
    }

    private fun getEiForFs(ei: Ei): EiForFs {
        return EiForFs(
                planning = EiForFsPlanning(
                        budget = EiForFsBudget(
                                amount = ei.planning.budget.amount
                        )
                )
        )
    }

    private fun getEntity(cpId: String, fs: Fs, owner: String, dateTime: LocalDateTime): FsEntity {
        val ocId = fs.ocid
        return FsEntity(
                cpId = cpId,
                ocId = ocId,
                token = generationService.generateRandomUUID(),
                owner = owner,
                createdDate = dateTime.toDate(),
                jsonData = toJson(fs),
                amount = fs.planning.budget.amount.amount,
                amountReserved = BigDecimal.ZERO
        )
    }

    companion object {
        private const val SEPARATOR = "-"
        private const val FS_SEPARATOR = "-FS-"
    }

}
