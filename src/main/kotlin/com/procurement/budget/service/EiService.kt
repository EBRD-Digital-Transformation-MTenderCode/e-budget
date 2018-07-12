package com.procurement.budget.service

import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.ei.BudgetEi
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.PlanningEi
import com.procurement.budget.model.dto.ei.TenderEi
import com.procurement.budget.model.dto.ei.request.EiCreate
import com.procurement.budget.model.dto.ei.request.EiUpdate
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
                 eiDto: EiCreate): ResponseDto

    fun updateEi(owner: String,
                 cpId: String,
                 token: String,
                 eiDto: EiUpdate): ResponseDto
}

@Service
class EiServiceImpl(private val ocdsProperties: OCDSProperties,
                    private val eiDao: EiDao,
                    private val generationService: GenerationService,
                    private val rulesService: RulesService) : EiService {

    override fun createEi(owner: String,
                          country: String,
                          dateTime: LocalDateTime,
                          eiDto: EiCreate): ResponseDto {
        validatePeriod(eiDto)
        validateCpv(country, eiDto)
        val cpId = getCpId(country)
        val ei = Ei(
                ocid = cpId,
                tender = TenderEi(
                        id = cpId,
                        title = eiDto.tender.title,
                        description = eiDto.tender.description,
                        status = TenderStatus.PLANNING,
                        statusDetails = TenderStatusDetails.EMPTY,
                        classification = eiDto.tender.classification,
                        mainProcurementCategory = eiDto.tender.mainProcurementCategory
                ),
                planning = PlanningEi(
                        budget = BudgetEi(
                                id = eiDto.tender.classification.id,
                                period = eiDto.planning.budget.period,
                                amount = null
                        ),
                        rationale = eiDto.planning.rationale
                ),
                buyer = eiDto.buyer.apply { id = identifier.scheme + SEPARATOR + identifier.id }
        )
        val entity = getEntity(ei, owner, dateTime)
        eiDao.save(entity)
        ei.token = entity.token.toString()
        return ResponseDto(true, null, ei)
    }

    override fun updateEi(owner: String,
                          cpId: String,
                          token: String,
                          eiDto: EiUpdate): ResponseDto {
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(ErrorType.INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(ErrorType.INVALID_OWNER)
        val ei = toObject(Ei::class.java, entity.jsonData)
        ei.apply {
            tender.title = eiDto.tender.title
            tender.description = eiDto.tender.description
            planning.rationale = eiDto.planning?.rationale
        }
        entity.jsonData = toJson(eiDto)
        eiDao.save(entity)
        return ResponseDto(true, null, ei)
    }

    private fun validateCpv(country: String, eiDto: EiCreate) {
        val cpvCodeRegex = rulesService.getCpvCodeRegex(country).toRegex()
        if (!cpvCodeRegex.matches(eiDto.tender.classification.id)) throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun validatePeriod(eiDto: EiCreate) {
        if (!eiDto.planning.budget.period.startDate.isBefore(eiDto.planning.budget.period.endDate))
            throw ErrorException(ErrorType.INVALID_PERIOD)
    }

    private fun getCpId(country: String): String {
        return ocdsProperties.prefix + SEPARATOR + country + SEPARATOR + generationService.getNowUtc()
    }

    private fun getEntity(ei: Ei, owner: String, dateTime: LocalDateTime): EiEntity {
        val ocId = ei.ocid
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
