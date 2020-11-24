package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Details
import com.procurement.budget.model.dto.ocds.Identifier
import com.procurement.budget.model.dto.ocds.Period
import com.procurement.budget.model.dto.ocds.Person
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import java.math.BigDecimal
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Ei @JsonCreator constructor(

        var token: String? = null,

        var ocid: String,

        var tender: TenderEi,

        var planning: PlanningEi,

        var buyer: OrganizationReferenceEi
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderEi @JsonCreator constructor(

        var id: String,

        var title: String,

        var description: String?,

        var status: TenderStatus,

        var statusDetails: TenderStatusDetails,

        val classification: Classification,

        val mainProcurementCategory: String,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("items") @field:JsonProperty("items") var items: List<ItemEI>?

)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlanningEi @JsonCreator constructor(

        val budget: BudgetEi,

        var rationale: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetEi @JsonCreator constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var id: String?,

        var period: Period,

        var amount: ValueEi?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValueEi @JsonCreator constructor(

        @field:JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        var currency: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceEi @JsonCreator constructor(

        var id: String?,

        val name: String,

        val identifier: Identifier,

        val address: Address,

        val contactPoint: ContactPoint,

        var additionalIdentifiers: HashSet<Identifier>?,

        var persones: HashSet<Person>?,

        var details: Details?,

        val buyerProfile: String?
)

data class ItemEI(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
        @param:JsonProperty("classification") @field:JsonProperty("classification") val classification: Classification,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("additionalClassifications") @field:JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>,

        @param:JsonProperty("deliveryAddress") @field:JsonProperty("deliveryAddress") val deliveryAddress: DeliveryAddress,
        @param:JsonProperty("quantity") @field:JsonProperty("quantity") val quantity: BigDecimal,
        @param:JsonProperty("unit") @field:JsonProperty("unit") val unit: Unit
) {
        data class Classification(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String
        )

        data class AdditionalClassification(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String
        )

        data class DeliveryAddress(
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("streetAddress") @field:JsonProperty("streetAddress") val streetAddress: String?,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("postalCode") @field:JsonProperty("postalCode") val postalCode: String?,

                @param:JsonProperty("addressDetails") @field:JsonProperty("addressDetails") val addressDetails: AddressDetails
        ) {
                data class AddressDetails(
                        @param:JsonProperty("country") @field:JsonProperty("country") val country: Country,
                        @param:JsonProperty("region") @field:JsonProperty("region") val region: Region,

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @param:JsonProperty("locality") @field:JsonProperty("locality") val locality: Locality?
                ) {
                        data class Country(
                                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                                @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                                @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                        )

                        data class Region(
                                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                                @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                                @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                        )

                        data class Locality(
                                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                                @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,

                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
                        )
                }
        }

        data class Unit(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String
        )
}