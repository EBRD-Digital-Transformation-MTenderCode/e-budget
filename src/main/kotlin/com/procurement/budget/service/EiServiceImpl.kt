package com.procurement.budget.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.milliNowUTC
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toObject
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface EiService {

    fun createEi(owner: String,
                 country: String,
                 dateTime: LocalDateTime,
                 ei: EiDto): ResponseDto<*>

    fun updateEi(owner: String,
                 cpId: String,
                 token: String,
                 eiDto: EiDto): ResponseDto<*>

    fun getEi(cpId: String): EiDto
}

@Service
class EiServiceImpl(private val ocdsProperties: OCDSProperties,
                    private val eiDao: EiDao,
                    private val generateServise: GenerateServise) : EiService {

    override fun createEi(owner: String,
                          country: String,
                          dateTime: LocalDateTime,
                          ei: EiDto): ResponseDto<*> {
        validatePeriod(ei)
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
                          eiDto: EiDto): ResponseDto<*> {
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(ErrorType.INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val ei = toObject(EiDto::class.java, entity.jsonData)
        ei.apply {
            planning = ei.planning
            tender = ei.tender
        }
        entity.jsonData = toJson(ei)
        eiDao.save(entity)
        return ResponseDto(true, null, ei)
    }

    override fun getEi(cpId: String): EiDto {
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        return toObject(EiDto::class.java, entity.jsonData)
    }

   private fun getCpId(country: String): String {
        return ocdsProperties.prefix + SEPARATOR + country + SEPARATOR + generateServise.getNowUtc()
    }

    private fun validatePeriod(ei: EiDto) {
        if (!ei.planning.budget.period.startDate.isBefore(ei.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun getEntity(ei: EiDto, owner: String, dateTime: LocalDateTime): EiEntity {
        val ocId = ei.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return EiEntity(
                cpId = ocId,
                token = generateServise.generateRandomUUID(),
                owner = owner,
                createdDate = dateTime.toDate(),
                jsonData = toJson(ei)
        )
    }

    companion object {
        private val SEPARATOR = "-"
    }

}
