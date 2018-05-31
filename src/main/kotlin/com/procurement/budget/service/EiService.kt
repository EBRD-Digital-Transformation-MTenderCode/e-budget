package com.procurement.budget.service

import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
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
                 ei: EiDto): ResponseDto<*>

//    fun updateEi(owner: String,
//                 cpId: String,
//                 token: String,
//                 eiDto: EiDto): ResponseDto<*>

    fun getEi(cpId: String): EiDto
}

@Service
class EiServiceImpl(private val ocdsProperties: OCDSProperties,
                    private val eiDao: EiDao,
                    private val generationService: GenerationService) : EiService {

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

//    override fun updateEi(owner: String,
//                          cpId: String,
//                          token: String,
//                          eiDto: EiDto): ResponseDto<*> {
//        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
//        if (entity.token != UUID.fromString(token)) throw ErrorException(ErrorType.INVALID_TOKEN)
//        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
//        val ei = toObject(EiDto::class.java, entity.jsonData)
//        ei.apply {
//            planning = eiDto.planning
//            tender = eiDto.tender
//        }
//        entity.jsonData = toJson(ei)
//        eiDao.save(entity)
//        ei.token = entity.token.toString()
//        return ResponseDto(true, null, ei)
//    }

    override fun getEi(cpId: String): EiDto {
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        return toObject(EiDto::class.java, entity.jsonData)
    }

    private fun getCpId(country: String): String {
        return ocdsProperties.prefix + SEPARATOR + country + SEPARATOR + generationService.getNowUtc()
    }

    private fun validatePeriod(ei: EiDto) {
        if (!ei.planning.budget.period.startDate.isBefore(ei.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
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
