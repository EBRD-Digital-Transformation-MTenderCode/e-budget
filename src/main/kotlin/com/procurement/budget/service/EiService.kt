package com.procurement.budget.service

import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.fs.FsDto
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.model.entity.FsEntity
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface EiService {

    fun createEi(owner: String,
                 country: String,
                 dateTime: LocalDateTime,
                 ei: Ei): ResponseDto

    fun updateEi(owner: String,
                 cpId: String,
                 token: String,
                 eiDto: Ei): ResponseDto
}

@Service
class EiServiceImpl(private val ocdsProperties: OCDSProperties,
                    private val eiDao: EiDao,
                    private val fsDao: FsDao,
                    private val generationService: GenerationService,
                    private val rulesService: RulesService) : EiService {

    override fun createEi(owner: String,
                          country: String,
                          dateTime: LocalDateTime,
                          ei: Ei): ResponseDto {
        filterForCreate(ei)
        validatePeriod(ei)
        validateCpv(country, ei)
        val cpId = getCpId(country)
        ei.apply {
            ocid = cpId
            tender.id = cpId
            tender.status = TenderStatus.PLANNING
            tender.statusDetails = TenderStatusDetails.EMPTY
            planning.budget.id = tender.classification.id
            buyer.apply { id = identifier.scheme + SEPARATOR + identifier.id }
        }
        val entity = getEntity(ei, owner, dateTime)
        eiDao.save(entity)
        ei.token = entity.token.toString()
        return ResponseDto(true, null, ei)
    }

    override fun updateEi(owner: String,
                          cpId: String,
                          token: String,
                          eiDto: Ei): ResponseDto {
        validateForUpdate(eiDto)
        validatePeriod(eiDto)
        val fsList = fsDao.getAllByCpId(cpId)
        if (fsList.isNotEmpty()) {
            validatePeriodWithFs(eiDto, fsList)
        }
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(ErrorType.INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val ei = toObject(Ei::class.java, entity.jsonData)
        ei.apply {
            tender.title = eiDto.tender.title
            tender.description = eiDto.tender.description
            planning.rationale = eiDto.planning.rationale
            planning.budget.period = eiDto.planning.budget.period
        }
        entity.jsonData = toJson(eiDto)
        eiDao.save(entity)
        eiDto.token = entity.token.toString()
        return ResponseDto(true, null, eiDto)
    }

    private fun validateCpv(country: String, dto: Ei) {
        val cpvCodeRegex = rulesService.getCpvCodeRegex(country).toRegex()
        if (!cpvCodeRegex.matches(dto.tender.classification.id)) throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun getCpId(country: String): String {
        return ocdsProperties.prefix + SEPARATOR + country + SEPARATOR + generationService.getNowUtc()
    }

    private fun validatePeriod(ei: Ei) {
        if (!ei.planning.budget.period.startDate.isBefore(ei.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun validatePeriodWithFs(ei: Ei, fsEntities: List<FsEntity>) {
        val (eiStartDate, eiEndDate) = ei.planning.budget.period
        fsEntities.forEach {
            val fs = toObject(FsDto::class.java, it.jsonData)
            val (fsStartDate, fsEndDate) = fs.planning.budget.period
            val fsPeriodValid = (fsStartDate.isAfter(eiStartDate) || fsStartDate.isEqual(eiStartDate))
                    && (fsEndDate.isBefore(eiEndDate) || fsEndDate.isEqual(eiEndDate))
            if (!fsPeriodValid) throw ErrorException(ErrorType.INVALID_PERIOD)
        }
    }

    private fun filterForCreate(ei: Ei) {
        if (ei.planning.budget.amount != null) ei.planning.budget.amount = null
    }

    private fun validateForUpdate(ei: Ei) {
        if (ei.planning.budget.id == null) throw ErrorException(ErrorType.INVALID_BUDGET_ID)
        if (ei.buyer.id == null) throw ErrorException(ErrorType.INVALID_BUYER_ID)
    }

    private fun getEntity(ei: Ei, owner: String, dateTime: LocalDateTime): EiEntity {
        val ocId = ei.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return EiEntity(
                cpId = ocId,
                token = generationService.generateRandomUUID(),
                owner = owner,
                createdDate = dateTime.toDate(),
                jsonData = toJson(ei)
        )
    }

    companion object {
        private val SEPARATOR = "-"
    }

}
