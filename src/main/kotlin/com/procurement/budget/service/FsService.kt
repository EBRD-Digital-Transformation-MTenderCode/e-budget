package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.CheckBudgetBreakdown
import com.procurement.budget.model.dto.check.CheckRequest
import com.procurement.budget.model.dto.check.CheckResponse
import com.procurement.budget.model.dto.check.CheckSourceParty
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.ei.ValueEi
import com.procurement.budget.model.dto.fs.*
import com.procurement.budget.model.dto.fs.request.FsCreate
import com.procurement.budget.model.dto.fs.response.EiForFs
import com.procurement.budget.model.dto.fs.response.EiForFsBudget
import com.procurement.budget.model.dto.fs.response.EiForFsPlanning
import com.procurement.budget.model.dto.fs.response.FsResponse
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.FsEntity
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface FsService {

    fun createFs(cpId: String,
                 owner: String,
                 dateTime: LocalDateTime,
                 fsDto: FsCreate): ResponseDto

    fun updateFs(cpId: String,
                 token: String,
                 owner: String,
                 fsDto: Fs): ResponseDto

    fun checkFs(dto: CheckRequest): ResponseDto
}

@Service
class FsServiceImpl(private val fsDao: FsDao,
                    private val eiDao: EiDao,
                    private val generationService: GenerationService) : FsService {

    override fun createFs(cpId: String,
                          owner: String,
                          dateTime: LocalDateTime,
                          fsDto: FsCreate): ResponseDto {
        validatePeriod(fsDto)
        validateEuropeanUnionFunding(fsDto)
        val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        val ei = toObject(Ei::class.java, eiEntity.jsonData)
        checkPeriodWithEi(ei, fsDto)
        checkCurrency(ei, fsDto)

        val tenderStatusFs: TenderStatus
        val funderFs: OrganizationReferenceFs?
        val sourceEntityFs: OrganizationReferenceFs
        val verifiedFs: Boolean
        if (fsDto.buyer != null) {
            funderFs = fsDto.buyer
            funderFs.apply { id = identifier?.scheme + SEPARATOR + identifier?.id }
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
        val fs = Fs(
                ocid = ocid,
                token = null,
                tender = TenderFs(
                        id = ocid,
                        status = tenderStatusFs,
                        statusDetails = TenderStatusDetails.EMPTY,
                        procuringEntity = null),
                planning = PlanningFs(
                        budget = BudgetFs(
                                sourceEntity = sourceEntityFs,
                                verified = verifiedFs,
                                amount = fsDto.planning.budget.amount,
                                isEuropeanUnionFunded = fsDto.planning.budget.isEuropeanUnionFunded,
                                europeanUnionFunding = fsDto.planning.budget.europeanUnionFunding,
                                description = fsDto.planning.budget.description,
                                period = fsDto.planning.budget.period,
                                id =


                        )
                ),
                buyer = null,
                funder = funderFs,
                payer = fsDto.tender.procuringEntity.apply { id = identifier?.scheme + SEPARATOR + identifier?.id }
        )
        val fsEntity = getEntity(cpId, fs, owner, dateTime)
        fsDao.save(fsEntity)
        fs.token = fsEntity.token.toString()
        //ei
        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
        ei.planning.budget.amount = ValueEi(amount = totalAmount, currency = fs.planning.budget.amount.currency)
        eiEntity.jsonData = toJson(ei)
        eiDao.save(eiEntity)
        return ResponseDto(true, null, FsResponse(getEiForFs(ei), fs))
    }

    override fun updateFs(cpId: String,
                          token: String,
                          owner: String,
                          fsDto: Fs): ResponseDto {
//        validatePeriod(fsDto)
//        val fsEntity = fsDao.getByCpIdAndToken(cpId, UUID.fromString(token))
//                ?: throw ErrorException(ErrorType.FS_NOT_FOUND)
//        if (fsEntity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
//        val fs = toObject(Fs::class.java, fsEntity.jsonData)
//        validateTenderId(fs, fsDto)
//        validateVerified(fs, fsDto)
//        val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
//        val ei = toObject(Ei::class.java, eiEntity.jsonData)
//        checkCurrency(ei, fsDto)
//        checkPeriodWithEi(ei, fsDto)
//        if (fs.tender.statusDetails != TenderStatusDetails.EMPTY) throw ErrorException(ErrorType.INVALID_STATUS)
//        when (fs.tender.status) {
//            TenderStatus.ACTIVE -> updateFsWhenStatusActive(fs, fsDto)
//            TenderStatus.PLANNING -> updateFsWhenStatusPlanning(fs, fsDto)
//            else -> throw ErrorException(ErrorType.INVALID_STATUS)
//        }
//        fsEntity.jsonData = toJson(fs)
//        fsDao.save(fsEntity)
//        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
//        var eiForFs: EiForFs? = null
//        if (totalAmount != ei.planning.budget.amount?.amount) {
//            ei.planning.budget.amount?.amount = totalAmount
//            eiEntity.jsonData = toJson(ei)
//            eiDao.save(eiEntity)
//            eiForFs = getEiForFs(ei)
//        }
//        return ResponseDto(true, null, FsResponse(eiForFs, fs))
    }

    override fun checkFs(dto: CheckRequest): ResponseDto {
        val budgetBreakdowns = dto.budgetBreakdown
        checkBudgetBreakdownCurrency(budgetBreakdowns)
        val cpIds = budgetBreakdowns.asSequence().map { getCpIdFromOcId(it.id) }.toSet()
        val funders = HashSet<OrganizationReferenceFs>()
        val payers = HashSet<OrganizationReferenceFs>()
        val buyers = HashSet<OrganizationReferenceEi>()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(ErrorType.FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        entities.asSequence()
                .map { toObject(Fs::class.java, it.jsonData) }
                .forEach { fsMap[it.ocid] = it }

        for (cpId in cpIds) {
            val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
            val ei = toObject(Ei::class.java, eiEntity.jsonData)
            checkCPV(ei, dto)
            buyers.add(ei.buyer)
            budgetBreakdowns.forEach { br ->
                val fs = fsMap[br.id] ?: throw ErrorException(ErrorType.FS_NOT_FOUND)
                checkFsStatus(fs)
                //checkTenderPeriod(fs, dto);
                checkFsAmount(fs, br)
                checkFsCurrency(fs, br)
                processBudgetBreakdown(br, fs)
                fs.funder?.let { funders.add(it) }
                fs.payer?.let { payers.add(it) }
            }
        }

        return ResponseDto(true, null,
                CheckResponse(
                        ei = cpIds,
                        budgetBreakdown = budgetBreakdowns,
                        funder = funders,
                        payer = payers,
                        buyer = buyers)
        )
    }

    private fun checkBudgetBreakdownCurrency(budgetBreakdown: List<CheckBudgetBreakdown>) {
        if (budgetBreakdown.asSequence().map { it.amount.currency }.toSet().size > 1) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun validatePeriod(fs: FsCreate) {
        if (!fs.planning.budget.period.startDate.isBefore(fs.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun validateEuropeanUnionFunding(fs: FsCreate) {
        if (fs.planning.budget.isEuropeanUnionFunded!!) {
            if (fs.planning.budget.europeanUnionFunding == null) throw ErrorException(ErrorType.INVALID_EUROPEAN)
        }
    }

    private fun validateTenderId(fs: Fs, fsDto: Fs) {
        if (fs.tender.id != fsDto.tender.id) throw ErrorException(ErrorType.INVALID_TENDER_ID)
    }

    private fun validateVerified(fs: Fs, fsDto: Fs) {
        if (fs.funder == null && fsDto.planning.budget.verified == null) throw ErrorException(ErrorType.INVALID_VERIFIED)
    }

    private fun checkPeriodWithEi(ei: Ei, fs: FsCreate) {
        val (eiStartDate, eiEndDate) = ei.planning.budget.period
        val (fsStartDate, fsEndDate) = fs.planning.budget.period
        val fsPeriodValid = (fsStartDate.isAfter(eiStartDate) || fsStartDate.isEqual(eiStartDate))
                && (fsEndDate.isBefore(eiEndDate) || fsEndDate.isEqual(eiEndDate))
        if (!fsPeriodValid) throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun checkCurrency(ei: Ei, fs: FsCreate) {
        val fsCurrency = fs.planning.budget.amount.currency
        val eiCurrency = ei.planning.budget.amount?.currency
        if (eiCurrency != null) {
            if (eiCurrency != fsCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
        }
    }

    private fun checkCPV(ei: Ei, dto: CheckRequest) {
        val eiCPV = ei.tender.classification.id
        val dtoCPV = dto.classification.id
        if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase())
            throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun getFounderFromEi(buyer: OrganizationReferenceEi): OrganizationReferenceFs {
        return OrganizationReferenceFs(
                id = buyer.id,
                name = buyer.name,
                identifier = buyer.identifier,
                address = buyer.address,
                additionalIdentifiers = buyer.additionalIdentifiers ?: hashSetOf(),
                contactPoint = buyer.contactPoint
        )
    }

    private fun getSourceEntity(funder: OrganizationReferenceFs): OrganizationReferenceFs {
        return OrganizationReferenceFs(
                id = funder.id,
                name = funder.name,
                identifier = null,
                address = null,
                additionalIdentifiers = null,
                contactPoint = null)
    }

    private fun processBudgetBreakdown(br: CheckBudgetBreakdown, fs: Fs) {
        val fsSe = fs.planning.budget.sourceEntity ?: throw ErrorException(ErrorType.INVALID_SOURCE_ENTITY)
        br.sourceParty = CheckSourceParty(id = fsSe.id, name = fsSe.name)
        br.period = fs.planning.budget.period
    }

    private fun checkFsCurrency(fs: Fs, br: CheckBudgetBreakdown) {
        val fsCurrency = fs.planning.budget.amount.currency
        val brCurrency = br.amount.currency
        if (fsCurrency != brCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkFsAmount(fs: Fs, br: CheckBudgetBreakdown) {
        val fsAmount = fs.planning.budget.amount.amount
        val brAmount = br.amount.amount
        if (brAmount.compareTo(fsAmount) == 1) throw ErrorException(ErrorType.INVALID_AMOUNT)
    }

    private fun checkFsStatus(fs: Fs) {
        val fsStatus = fs.tender.status ?: throw ErrorException(ErrorType.INVALID_STATUS)
        val fsStatusDetails = fs.tender.statusDetails ?: throw ErrorException(ErrorType.INVALID_STATUS)
        if (!((fsStatus == TenderStatus.ACTIVE || fsStatus == TenderStatus.PLANNING || fsStatus == TenderStatus.PLANNED)
                        && fsStatusDetails == TenderStatusDetails.EMPTY))
            throw ErrorException(ErrorType.INVALID_STATUS)
    }

    private fun getOcId(cpId: String): String {
        return cpId + FS_SEPARATOR + generationService.getNowUtc()
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    private fun updateFsWhenStatusPlanning(fs: Fs, fsUpdateDto: Fs) {
        fs.planning.apply {
            rationale = fsUpdateDto.planning.rationale
            budget.apply {
                description = fsUpdateDto.planning.budget.description
                amount.amount = fsUpdateDto.planning.budget.amount.amount
                europeanUnionFunding = fsUpdateDto.planning.budget.europeanUnionFunding
                verified = fsUpdateDto.planning.budget.verified
                verificationDetails = fsUpdateDto.planning.budget.verificationDetails
            }
        }
    }

    private fun updateFsWhenStatusActive(fs: Fs, fsUpdateDto: Fs) {
        updateFsWhenStatusPlanning(fs, fsUpdateDto)
        fs.apply { planning.budget.id = fsUpdateDto.planning.budget.id }
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
        val ocId = fs.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
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
        private val SEPARATOR = "-"
        private val FS_SEPARATOR = "-FS-"
    }

}
