package com.procurement.budget.service

import com.procurement.budget.config.properties.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType.CONTEXT
import com.procurement.budget.exception.ErrorType.EI_NOT_FOUND
import com.procurement.budget.exception.ErrorType.INVALID_CPV
import com.procurement.budget.exception.ErrorType.INVALID_OWNER
import com.procurement.budget.exception.ErrorType.INVALID_PERIOD
import com.procurement.budget.exception.ErrorType.INVALID_TOKEN
import com.procurement.budget.model.dto.bpe.CommandMessage
import com.procurement.budget.model.dto.bpe.ResponseDto
import com.procurement.budget.model.dto.ei.BudgetEi
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.ItemEI
import com.procurement.budget.model.dto.ei.PlanningEi
import com.procurement.budget.model.dto.ei.TenderEi
import com.procurement.budget.model.dto.ei.request.EiCreate
import com.procurement.budget.model.dto.ei.request.EiUpdate
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toLocal
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class EiService(
    private val ocdsProperties: OCDSProperties,
    private val eiDao: EiDao,
    private val generationService: GenerationService,
    private val rulesService: RulesService
) {

    fun createEi(cm: CommandMessage): ResponseDto {
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val country = cm.context.country ?: throw ErrorException(CONTEXT)
        val dateTime = cm.context.startDate?.toLocal() ?: throw ErrorException(CONTEXT)
        val testMode: Boolean = cm.context.testMode?.let { it } ?: false
        val eiDto = toObject(EiCreate::class.java, cm.data)
        validateDto(eiDto)
        validatePeriod(eiDto)
        validateCpv(country, eiDto)
        validateItems(eiDto)
        val cpId = getCpId(country, testMode)
        val ei = createEi(cpId, eiDto)
        val entity = getEntity(ei, owner, dateTime)
        eiDao.save(entity)
        ei.token = entity.token.toString()
        return ResponseDto(data = ei)
    }

    fun updateEi(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid ?: throw ErrorException(CONTEXT)
        val owner = cm.context.owner ?: throw ErrorException(CONTEXT)
        val token = cm.context.token ?: throw ErrorException(CONTEXT)
        val eiDto = toObject(EiUpdate::class.java, cm.data)
        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(INVALID_OWNER)
        val ei = toObject(Ei::class.java, entity.jsonData)
        ei.apply {
            tender.title = eiDto.tender.title
            tender.description = eiDto.tender.description
            planning.rationale = eiDto.planning?.rationale
        }
        entity.jsonData = toJson(ei)
        eiDao.save(entity)
        return ResponseDto(data = ei)
    }

    private fun validateDto(eiDto: EiCreate) {
        val details = eiDto.buyer.details
        if (details != null) {
            if (details.typeOfBuyer == null && details.mainGeneralActivity == null && details.mainSectoralActivity == null) {
                eiDto.buyer.details = null
            }
        }
    }

    private fun validateCpv(country: String, eiDto: EiCreate) {
        val cpvCodeRegex = rulesService.getCpvCodeRegex(country).toRegex()
        if (!cpvCodeRegex.matches(eiDto.tender.classification.id)) throw ErrorException(INVALID_CPV)
    }

    private fun validatePeriod(eiDto: EiCreate) {
        if (eiDto.planning.budget.period.startDate >= eiDto.planning.budget.period.endDate)
            throw ErrorException(INVALID_PERIOD)
    }

    private fun validateItems(eiDto: EiCreate) {
        val classificationStartingSymbols = eiDto.tender.classification.id.slice(0..2)
        val invalidClassifications = eiDto.tender.items
            ?.map { it.classification.id }
            ?.filter { !it.startsWith(prefix = classificationStartingSymbols, ignoreCase = true) }
            .orEmpty()
        if (invalidClassifications.isNotEmpty())
            throw ErrorException(
                error = INVALID_CPV,
                message = "Invalid CPV code in classification(s) '${invalidClassifications.joinToString()}'"
            )
    }

    private fun getCpId(country: String, testMode: Boolean): String {
        val prefix: String = if (testMode) ocdsProperties.prefixes!!.test!! else ocdsProperties.prefixes!!.main!!
        return prefix + SEPARATOR + country + SEPARATOR + generationService.getNowUtc()
    }

    private fun createEi(
        cpId: String,
        eiDto: EiCreate
    ): Ei {
        return Ei(
            ocid = cpId,
            tender = TenderEi(
                id = generationService.generateTenderId().toString(),
                title = eiDto.tender.title,
                description = eiDto.tender.description,
                status = TenderStatus.PLANNING,
                statusDetails = TenderStatusDetails.EMPTY,
                classification = eiDto.tender.classification,
                mainProcurementCategory = eiDto.tender.mainProcurementCategory,
                items = eiDto.tender.items?.map { item ->
                    ItemEI(
                        id = item.id,
                        description = item.description,
                        classification = item.classification.let { classification ->
                            ItemEI.Classification(
                                id = classification.id
                            )
                        },
                        additionalClassifications = item.additionalClassifications
                            .map { additionalClassification ->
                                ItemEI.AdditionalClassification(
                                    id = additionalClassification.id
                                )
                            },
                        deliveryAddress = item.deliveryAddress.let { address ->
                            ItemEI.DeliveryAddress(
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode,
                                addressDetails = address.addressDetails.let { addressDetails ->
                                    ItemEI.DeliveryAddress.AddressDetails(
                                        country = addressDetails.country.let { country ->
                                            ItemEI.DeliveryAddress.AddressDetails.Country(
                                                id = country.id,
                                                description = country.description,
                                                scheme = country.scheme
                                            )
                                        },
                                        region = addressDetails.region.let { region ->
                                            ItemEI.DeliveryAddress.AddressDetails.Region(
                                                id = region.id,
                                                description = region.description,
                                                scheme = region.scheme
                                            )
                                        },
                                        locality = addressDetails.locality?.let { locality ->
                                            ItemEI.DeliveryAddress.AddressDetails.Locality(
                                                id = locality.id,
                                                description = locality.description,
                                                scheme = locality.scheme
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        quantity = item.quantity,
                        unit = item.unit.let { unit ->
                            ItemEI.Unit(
                                id = unit.id
                            )
                        }
                    )
                }
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
        private const val SEPARATOR = "-"
    }
}
