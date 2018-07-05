package com.procurement.budget.service

import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.CheckBudgetBreakdownDto
import com.procurement.budget.model.dto.check.CheckRequestDto
import com.procurement.budget.model.dto.check.CheckResponseDto
import com.procurement.budget.model.dto.check.CheckSourcePartyDto
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.*
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
                 fsDto: FsRequestCreateDto): ResponseDto

    fun updateFs(cpId: String,
                 token: String,
                 owner: String,
                 fsUpdateDto: FsRequestUpdateDto): ResponseDto

    fun checkFs(dto: CheckRequestDto): ResponseDto
}

@Service
class FsServiceImpl(private val fsDao: FsDao,
                    private val eiService: EiService,
                    private val generationService: GenerationService) : FsService {

    override fun createFs(cpId: String,
                          owner: String,
                          dateTime: LocalDateTime,
                          fsDto: FsRequestCreateDto): ResponseDto {
        val ei = eiService.getEi(cpId)
        checkCurrency(ei, fsDto)
        checkPeriod(ei, fsDto)
        validatePeriod(fsDto)
        val fsTenderStatus: TenderStatus
        val fsFunder: FsOrganizationReferenceDto?
        val fsSourceEntity: FsOrganizationReferenceDto
        val fsVerified: Boolean
        if (fsDto.buyer != null) {
            fsFunder = fsDto.buyer
            fsFunder.apply { id = identifier?.scheme + SEPARATOR + identifier?.id }
            fsSourceEntity = getSourceEntity(fsFunder)
            fsVerified = true
            fsTenderStatus = TenderStatus.ACTIVE
        } else {
            fsFunder = null
            fsSourceEntity = getSourceEntity(getFounderFromEi(ei.buyer))
            fsVerified = false
            fsTenderStatus = TenderStatus.PLANNING
        }
        val fs = FsDto(
                ocid = getOcId(cpId),
                token = null,
                tender = FsTenderDto(
                        id = cpId,
                        status = fsTenderStatus,
                        statusDetails = TenderStatusDetails.EMPTY,
                        procuringEntity = null),
                planning = fsDto.planning,
                funder = fsFunder,
                payer = fsDto.tender.procuringEntity
        )
        fs.apply {
            planning.budget.apply {
                sourceEntity = fsSourceEntity
                verified = fsVerified
            }
            payer?.apply { id = identifier?.scheme + SEPARATOR + identifier?.id }
        }
        val entity = getEntity(cpId, fs, owner, dateTime)
        fsDao.save(entity)
        fs.token = entity.token.toString()
        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
        return ResponseDto(true, null, FsResponseDto(totalAmount, fs))
    }

    override fun updateFs(cpId: String,
                          token: String,
                          owner: String,
                          fsUpdateDto: FsRequestUpdateDto): ResponseDto {
        val entity = fsDao.getByCpIdAndToken(cpId, UUID.fromString(token))
                ?: throw ErrorException(ErrorType.FS_NOT_FOUND)
        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val fs = toObject(FsDto::class.java, entity.jsonData)

        validateAndUpdateFS(fs, fsUpdateDto)

        entity.jsonData = toJson(fs)
        fsDao.save(entity)
        val totalAmount = fsDao.getTotalAmountByCpId(cpId) ?: BigDecimal.ZERO
        return ResponseDto(true, null, FsResponseDto(totalAmount, fs))
    }

    override fun checkFs(dto: CheckRequestDto): ResponseDto {
        val budgetBreakdowns = dto.budgetBreakdown
        checkBudgetBreakdownCurrency(budgetBreakdowns)
        val cpIds = budgetBreakdowns.asSequence().map { getCpIdFromOcId(it.id) }.toSet()
        val funders = HashSet<FsOrganizationReferenceDto>()
        val payers = HashSet<FsOrganizationReferenceDto>()
        val buyers = HashSet<EiOrganizationReferenceDto>()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(ErrorType.FS_NOT_FOUND)
        val fsMap = HashMap<String?, FsDto>()
        entities.asSequence()
                .map { toObject(FsDto::class.java, it.jsonData) }
                .forEach { fsMap[it.ocid] = it }

        for (cpId in cpIds) {
            val ei = eiService.getEi(cpId)
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
                CheckResponseDto(
                        ei = cpIds,
                        budgetBreakdown = budgetBreakdowns,
                        funder = funders,
                        payer = payers,
                        buyer = buyers)
        )
    }

    private fun checkBudgetBreakdownCurrency(budgetBreakdown: List<CheckBudgetBreakdownDto>) {
        if (budgetBreakdown.asSequence().map { it.amount.currency }.toSet().size > 1) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun validatePeriod(fs: FsRequestCreateDto) {
        if (!fs.planning.budget.period.startDate.isBefore(fs.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun checkPeriod(ei: EiDto, fs: FsRequestCreateDto) {
        val (eiStartDate, eiEndDate) = ei.planning.budget.period
        val (fsStartDate, fsEndDate) = fs.planning.budget.period
        val fsPeriodValid = (fsStartDate.isAfter(eiStartDate) || fsStartDate.isEqual(eiStartDate))
                && (fsEndDate.isBefore(eiEndDate) || fsEndDate.isEqual(eiEndDate))
        if (!fsPeriodValid) throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun checkCurrency(ei: EiDto, fs: FsRequestCreateDto) {
//        val eiCurrency = ei.planning.budget.amount.currency
//        val fsCurrency = fs.planning.budget.amount.currency
//        if (eiCurrency != fsCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkCPV(ei: EiDto, dto: CheckRequestDto) {
        val eiCPV = ei.tender.classification.id
        val dtoCPV = dto.classification.id
        if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase())
            throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun getFounderFromEi(buyer: EiOrganizationReferenceDto): FsOrganizationReferenceDto {
        return FsOrganizationReferenceDto(
                id = buyer.id,
                name = buyer.name,
                identifier = buyer.identifier,
                address = buyer.address,
                additionalIdentifiers = buyer.additionalIdentifiers ?: hashSetOf(),
                contactPoint = buyer.contactPoint
        )
    }

    private fun getSourceEntity(funder: FsOrganizationReferenceDto): FsOrganizationReferenceDto {
        return FsOrganizationReferenceDto(
                id = funder.id,
                name = funder.name,
                identifier = null,
                address = null,
                additionalIdentifiers = null,
                contactPoint = null)
    }

    private fun processBudgetBreakdown(br: CheckBudgetBreakdownDto, fs: FsDto) {
        val fsSe = fs.planning.budget.sourceEntity ?: throw ErrorException(ErrorType.INVALID_SOURCE_ENTITY)
        br.sourceParty = CheckSourcePartyDto(id = fsSe.id, name = fsSe.name)
        br.period = fs.planning.budget.period
    }

    private fun checkTenderPeriod(fs: FsDto, dto: CheckRequestDto) {
        val tenderStartDate = dto.tenderPeriod.startDate.toLocalDate()
        val fsEndDate = fs.planning.budget.period.endDate.toLocalDate()
        if (!(tenderStartDate.isBefore(fsEndDate) || tenderStartDate.isEqual(fsEndDate))) {
            throw ErrorException(ErrorType.INVALID_DATE)
        }
    }

    private fun checkFsCurrency(fs: FsDto, br: CheckBudgetBreakdownDto) {
        val fsCurrency = fs.planning.budget.amount.currency
        val brCurrency = br.amount.currency
        if (fsCurrency != brCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkFsAmount(fs: FsDto, br: CheckBudgetBreakdownDto) {
        val fsAmount = fs.planning.budget.amount.amount
        val brAmount = br.amount.amount
        if (brAmount.compareTo(fsAmount) == 1) throw ErrorException(ErrorType.INVALID_AMOUNT)
    }

    private fun checkFsStatus(fs: FsDto) {
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

    private fun getAmount(fs: FsDto): BigDecimal {
        return fs.planning.budget.amount.amount
    }

    private fun getEntity(cpId: String, fs: FsDto, owner: String, dateTime: LocalDateTime): FsEntity {
        val ocId = fs.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return FsEntity(
                cpId = cpId,
                ocId = ocId,
                token = generationService.generateRandomUUID(),
                owner = owner,
                createdDate = dateTime.toDate(),
                jsonData = toJson(fs),
                amount = getAmount(fs),
                amountReserved = BigDecimal.ZERO
        )
    }

    companion object {
        private val SEPARATOR = "-"
        private val FS_SEPARATOR = "-FS-"
    }

    private fun validateAndUpdateFS(fs: FsDto, fsUpdateDto: FsRequestUpdateDto) {
        val tenderStatus = fs.tender.status

        if (tenderStatus != TenderStatus.ACTIVE || tenderStatus != TenderStatus.PLANNING) throw ErrorException(ErrorType.INVALID_STATUS)//current status

        if (tenderStatus == TenderStatus.ACTIVE) {
            updateFsWhenStatusActive(fs, fsUpdateDto)
        } else if (tenderStatus == TenderStatus.PLANNING) {
            updateFsWhenStatusPlanning(fs, fsUpdateDto)
        }

    }

    private fun updateFsWhenStatusPlanning(fs: FsDto, fsUpdateDto: FsRequestUpdateDto) {
        fs.planning.apply {
            rationale = fsUpdateDto.planning.rationale
            budget.apply {
                description = fsUpdateDto.planning.budget.description
                amount.amount = fsUpdateDto.planning.budget.amount.amount
                europeanUnionFunding = fsUpdateDto.planning.budget.europeanUnionFunding
//                isEuropeanUnionFunded = fsUpdateDto.planning.budget.isEuropeanUnionFunded
                verified = fsUpdateDto.planning.budget.verified
                verificationDetails = fsUpdateDto.planning.budget.verificationDetails
            }
        }

    }

    private fun updateFsWhenStatusActive(fs: FsDto, fsUpdateDto: FsRequestUpdateDto) {
        updateFsWhenStatusPlanning(fs, fsUpdateDto)
        fs.apply { planning.budget.id = fsUpdateDto.planning.budget.id }
    }


}
