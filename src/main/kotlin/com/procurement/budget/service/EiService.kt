package com.procurement.budget.service

import com.procurement.budget.config.properties.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.exception.ErrorType.CONTEXT
import com.procurement.budget.exception.ErrorType.EI_NOT_FOUND
import com.procurement.budget.exception.ErrorType.INVALID_CPV
import com.procurement.budget.exception.ErrorType.INVALID_OWNER
import com.procurement.budget.exception.ErrorType.INVALID_PERIOD
import com.procurement.budget.exception.ErrorType.INVALID_TOKEN
import com.procurement.budget.lib.errorIfBlank
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
import com.procurement.budget.utils.getDuplicate
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toJson
import com.procurement.budget.utils.toLocal
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.math.BigDecimal
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
        eiDto.validateTextAttributes()
        eiDto.validateDuplicates()

        val entity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
        if (entity.token != UUID.fromString(token)) throw ErrorException(INVALID_TOKEN)
        if (entity.owner != owner) throw ErrorException(INVALID_OWNER)
        val ei = toObject(Ei::class.java, entity.jsonData)
        validateItems(ei, eiDto)

        val updatedItems = getUpdatedItems(ei, eiDto)

        ei.apply {
            tender.title = eiDto.tender.title
            tender.description = eiDto.tender.description
            tender.items = updatedItems
            planning.rationale = eiDto.planning?.rationale
        }

        entity.jsonData = toJson(ei)
        eiDao.save(entity)
        return ResponseDto(data = ei)
    }

    private fun getUpdatedItems(ei: Ei, eiDto: EiUpdate): List<ItemEI> {
        val storedItems = ei.tender.items?.associateBy { it.id }.orEmpty()

        return eiDto.tender.items
            ?.map { item ->
                storedItems[item.id]
                    ?.let { storedItem -> updateStoredItem(storedItem, item) }
                    ?: createItem(item)
            }.orEmpty()
    }

    private fun createItem(item: EiUpdate.TenderEiUpdate.Item) =
        ItemEI(
            id = generationService.generateItemId().toString(),
            description = item.description,
            classification = item.classification.let { classification ->
                ItemEI.Classification(
                    id = classification.id,
                    description = classification.description,
                    scheme = classification.scheme
                )
            },
            additionalClassifications = item.additionalClassifications
                ?.map { additionalClassification ->
                    ItemEI.AdditionalClassification(
                        id = additionalClassification.id,
                        scheme = additionalClassification.scheme,
                        description = additionalClassification.description
                    )
                }
                .orEmpty(),
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
                                    scheme = country.scheme,
                                    uri = country.uri
                                )
                            },
                            region = addressDetails.region.let { region ->
                                ItemEI.DeliveryAddress.AddressDetails.Region(
                                    id = region.id,
                                    description = region.description,
                                    scheme = region.scheme,
                                    uri = region.uri
                                )
                            },
                            locality = addressDetails.locality?.let { locality ->
                                ItemEI.DeliveryAddress.AddressDetails.Locality(
                                    id = locality.id,
                                    description = locality.description,
                                    scheme = locality.scheme,
                                    uri = locality.uri
                                )
                            }
                        )
                    }
                )
            },
            quantity = item.quantity,
            unit = item.unit.let { unit ->
                ItemEI.Unit(
                    id = unit.id,
                    name = unit.name
                )
            }
        )

    private fun updateStoredItem(storedItem: ItemEI, item: EiUpdate.TenderEiUpdate.Item) =
        storedItem.copy(
            description = item.description,
            classification = item.classification.let { classification ->
                ItemEI.Classification(
                    id = classification.id,
                    scheme = classification.scheme,
                    description = classification.description
                )
            },
            additionalClassifications = item.additionalClassifications
                ?.map { additionalClassification ->
                    ItemEI.AdditionalClassification(
                        id = additionalClassification.id,
                        scheme = additionalClassification.scheme,
                        description = additionalClassification.description
                    )
                }
                ?: storedItem.additionalClassifications,
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
                                    scheme = country.scheme,
                                    uri = country.uri
                                )
                            },
                            region = addressDetails.region.let { region ->
                                ItemEI.DeliveryAddress.AddressDetails.Region(
                                    id = region.id,
                                    description = region.description,
                                    scheme = region.scheme,
                                    uri = region.uri
                                )
                            },
                            locality = addressDetails.locality?.let { locality ->
                                ItemEI.DeliveryAddress.AddressDetails.Locality(
                                    id = locality.id,
                                    description = locality.description,
                                    scheme = locality.scheme,
                                    uri = locality.uri
                                )
                            }
                        )
                    }
                )
            },
            quantity = item.quantity,
            unit = item.unit.let { unit ->
                ItemEI.Unit(
                    id = unit.id,
                    name = unit.name
                )
            }
        )

    private fun validateDto(eiDto: EiCreate) {
        eiDto.validateTextAttributes()
        eiDto.validateDuplicates()

        val details = eiDto.buyer.details
        if (details != null) {
            if (details.typeOfBuyer == null && details.mainGeneralActivity == null && details.mainSectoralActivity == null) {
                eiDto.buyer.details = null
            }
        }
    }

    private fun EiCreate.validateTextAttributes() {
        planning.rationale.checkForBlank("planning.rationale")

        tender.title.checkForBlank("tender.title")
        tender.description.checkForBlank("tender.description")
        tender.items?.forEach {item ->
            item.description.checkForBlank("tender.items.description")
            item.deliveryAddress.streetAddress.checkForBlank("tender.items.deliveryAddress.streetAddress")
            item.deliveryAddress.postalCode.checkForBlank("tender.items.deliveryAddress.postalCode")
            item.deliveryAddress.addressDetails.locality?.scheme.checkForBlank("deliveryAddress.addressDetails.locality.scheme")
            item.deliveryAddress.addressDetails.locality?.id.checkForBlank("deliveryAddress.addressDetails.locality.id")
            item.deliveryAddress.addressDetails.locality?.description.checkForBlank("deliveryAddress.addressDetails.locality.description")
        }
            
        buyer.additionalIdentifiers
            ?.forEach { additionalIdentifier ->
            additionalIdentifier.id.checkForBlank("buyer.additionalIdentifiers.id")
            additionalIdentifier.scheme.checkForBlank("buyer.additionalIdentifiers.scheme")
            additionalIdentifier.legalName.checkForBlank("buyer.additionalIdentifiers.legalName")
            additionalIdentifier.uri.checkForBlank("buyer.additionalIdentifiers.uri")
        }

        buyer.address.addressDetails.locality.description.checkForBlank("buyer.address.addressDetails.locality.description")
        buyer.address.addressDetails.locality.id.checkForBlank("buyer.address.addressDetails.locality.id")
        buyer.address.addressDetails.locality.scheme.checkForBlank("buyer.address.addressDetails.locality.scheme")
        buyer.address.postalCode.checkForBlank("buyer.address.postalCode")
        buyer.address.streetAddress.checkForBlank("buyer.address.streetAddress")
        buyer.contactPoint.email.checkForBlank("buyer.contactPoint.email")
        buyer.contactPoint.faxNumber.checkForBlank("buyer.contactPoint.faxNumber")
        buyer.contactPoint.name.checkForBlank("buyer.contactPoint.name")
        buyer.contactPoint.telephone.checkForBlank("buyer.contactPoint.telephone")
        buyer.contactPoint.url.checkForBlank("buyer.contactPoint.url")
        buyer.identifier.id.checkForBlank("buyer.identifier.id")
        buyer.identifier.legalName.checkForBlank("buyer.identifier.legalName")
        buyer.identifier.uri.checkForBlank("buyer.identifier.uri")
        buyer.name.checkForBlank("buyer.name")
    }

    private fun EiUpdate.validateTextAttributes() {
        planning?.rationale.checkForBlank("planning.rationale")

        tender.title.checkForBlank("tender.title")
        tender.description.checkForBlank("tender.description")

        tender.items?.forEach {item ->
            item.description.checkForBlank("tender.items.description")
            item.deliveryAddress.streetAddress.checkForBlank("tender.items.deliveryAddress.streetAddress")
            item.deliveryAddress.postalCode.checkForBlank("tender.items.deliveryAddress.postalCode")
            item.deliveryAddress.addressDetails.locality?.scheme.checkForBlank("deliveryAddress.addressDetails.locality.scheme")
            item.deliveryAddress.addressDetails.locality?.id.checkForBlank("deliveryAddress.addressDetails.locality.id")
            item.deliveryAddress.addressDetails.locality?.description.checkForBlank("deliveryAddress.addressDetails.locality.description")
        }
    }

    private fun String?.checkForBlank(name: String) = this.errorIfBlank {
        ErrorException(
            error = ErrorType.INCORRECT_VALUE_ATTRIBUTE,
            message = "The attribute '$name' is empty or blank."
        )
    }

    private fun EiCreate.validateDuplicates() {
        val duplicateItemId = tender.items
            ?.getDuplicate { it.id.toUpperCase() }

        if (duplicateItemId != null)
            throw ErrorException(
                error = ErrorType.DUPLICATE,
                message = "Attribute 'tender.items' has duplicate by id '${duplicateItemId}'."
            )

        val duplicateAdditionalClassification = tender.items
            ?.asSequence()
            ?.flatMap {
                it.additionalClassifications?.asSequence() ?: emptySequence()
            }
            ?.getDuplicate { it.scheme.toUpperCase() + it.id.toUpperCase() }

        if (duplicateAdditionalClassification != null)
            throw ErrorException(
                error = ErrorType.DUPLICATE,
                message = "Attribute 'tender.items.additionalClassifications' has duplicate by scheme '${duplicateAdditionalClassification.scheme}' and id '${duplicateAdditionalClassification.id}'."
            )

        val duplicateAdditionalIdentifiers = buyer.additionalIdentifiers
            ?.getDuplicate { it.scheme.toUpperCase() + it.id.toUpperCase() }

        if (duplicateAdditionalIdentifiers != null)
            throw ErrorException(
                error = ErrorType.DUPLICATE,
                message = "Attribute 'buyer.additionalIdentifiers' has duplicate by scheme '${duplicateAdditionalIdentifiers.scheme}' and id '${duplicateAdditionalIdentifiers.id}'."
            )
    }

    private fun EiUpdate.validateDuplicates() {
        val duplicateAdditionalClassification = tender.items
            ?.asSequence()
            ?.flatMap {
                it.additionalClassifications?.asSequence() ?: emptySequence()
            }
            ?.getDuplicate { it.scheme.toUpperCase() + it.id.toUpperCase() }

        if (duplicateAdditionalClassification != null)
            throw ErrorException(
                error = ErrorType.DUPLICATE,
                message = "Attribute 'tender.items.additionalClassifications' has duplicate by scheme '${duplicateAdditionalClassification.scheme}' and id '${duplicateAdditionalClassification.id}'."
            )
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

    private fun validateItems(ei: Ei, eiDto: EiUpdate) {
        checkClassification(ei, eiDto)
        checkItemsQuantity(eiDto)
        checkItemsForDuplicates(eiDto)
    }

    private fun checkClassification(
        ei: Ei,
        eiDto: EiUpdate
    ) {
        val classificationStartingSymbols = ei.tender.classification.id.slice(0..2)

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

    private fun checkItemsQuantity(eiDto: EiUpdate) {
        val itemWithWrongQuantity = eiDto.tender.items?.firstOrNull { it.quantity <= BigDecimal.ZERO }
        if (itemWithWrongQuantity != null)
            throw ErrorException(
                error = ErrorType.INVALID_ITEM_QUANTITY,
                message = "Quantity of item '${itemWithWrongQuantity.id}' must be greater than zero"
            )
    }

    private fun checkItemsForDuplicates(eiDto: EiUpdate) {
        val duplicateItem = eiDto.tender.items?.getDuplicate { it.id }
        if (duplicateItem != null)
            throw ErrorException(
                error = ErrorType.DUPLICATED_ITEMS,
                message = "Item '${duplicateItem.id}' has a duplicate"
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
                        id = generationService.generateItemId().toString(),
                        description = item.description,
                        classification = item.classification.let { classification ->
                            ItemEI.Classification(
                                id = classification.id,
                                scheme = classification.scheme,
                                description = classification.description
                            )
                        },
                        additionalClassifications = item.additionalClassifications
                            ?.map { additionalClassification ->
                                ItemEI.AdditionalClassification(
                                    id = additionalClassification.id,
                                    scheme = additionalClassification.scheme,
                                    description = additionalClassification.description
                                )
                            }.orEmpty(),
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
                                                scheme = country.scheme,
                                                uri = country.uri
                                            )
                                        },
                                        region = addressDetails.region.let { region ->
                                            ItemEI.DeliveryAddress.AddressDetails.Region(
                                                id = region.id,
                                                description = region.description,
                                                scheme = region.scheme,
                                                uri = region.uri
                                            )
                                        },
                                        locality = addressDetails.locality?.let { locality ->
                                            ItemEI.DeliveryAddress.AddressDetails.Locality(
                                                id = locality.id,
                                                description = locality.description,
                                                scheme = locality.scheme,
                                                uri = locality.uri
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        quantity = item.quantity,
                        unit = item.unit.let { unit ->
                            ItemEI.Unit(
                                id = unit.id,
                                name = unit.name
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
