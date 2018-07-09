package com.procurement.budget.service

import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.EiEntity
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
                 ei: EiDto): ResponseDto

    fun updateEi(owner: String,
                 cpId: String,
                 token: String,
                 updatableEi: EiDto): ResponseDto

    fun getEi(cpId: String): EiDto
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
                          ei: EiDto): ResponseDto {
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
                          updatableEi: EiDto): ResponseDto {
        validateForUpdate(updatableEi)
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(ErrorType.INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val ei = toObject(EiDto::class.java, entity.jsonData)
        val fsExist = fsDao.getCountByCpId(cpId) > 0
        updatableEi.apply {
            ocid = ei.ocid
            tender.id = ei.tender.id
            tender.classification.scheme = ei.tender.classification.scheme
            buyer = ei.buyer
            if (fsExist) {
                checkCPVAndSetBudgetId(ei, updatableEi)
            }
        }
        entity.jsonData = toJson(updatableEi)
        eiDao.save(entity)
        updatableEi.token = entity.token.toString()
        return ResponseDto(true, null, updatableEi)
    }

    override fun getEi(cpId: String): EiDto {
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        return toObject(EiDto::class.java, entity.jsonData)
    }

    private fun validateCpv(country: String, dto: EiDto) {
        val cpvCodeRegex = rulesService.getCpvCodeRegex(country).toRegex()
        if (!cpvCodeRegex.matches(dto.tender.classification.id)) throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun getCpId(country: String): String {
        return ocdsProperties.prefix + SEPARATOR + country + SEPARATOR + generationService.getNowUtc()
    }

    private fun validatePeriod(ei: EiDto) {
        if (!ei.planning.budget.period.startDate.isBefore(ei.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun checkCPVAndSetBudgetId(ei: EiDto, dto: EiDto) {
        val eiCPV = ei.tender.classification.id
        val dtoCPV = dto.tender.classification.id
        if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase())
            throw ErrorException(ErrorType.INVALID_CPV)
        if (eiCPV != dtoCPV) ei.planning.budget.id = generationService.getNowUtc().toString()
    }

    private fun validateForUpdate(ei: EiDto) {
        if (ei.planning.budget.id == null) throw ErrorException(ErrorType.INVALID_BUDGET_ID)
        if (ei.buyer.id == null) throw ErrorException(ErrorType.INVALID_BUYER_ID)
    }

    private fun getEntity(ei: EiDto, owner: String, dateTime: LocalDateTime): EiEntity {
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
