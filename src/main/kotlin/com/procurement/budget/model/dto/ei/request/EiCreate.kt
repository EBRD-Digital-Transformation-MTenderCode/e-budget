package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.Period
import java.math.BigDecimal

data class EiCreate @JsonCreator constructor(

    var tender: TenderEiCreate,

    var planning: PlanningEiCreate,

    var buyer: OrganizationReferenceEi
) {

    data class TenderEiCreate @JsonCreator constructor(

        var title: String,

        var description: String?,

        val classification: Classification,

        val mainProcurementCategory: String,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("items") @field:JsonProperty("items") val items: List<Item>?
    ) {

        data class Item(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
            @param:JsonProperty("classification") @field:JsonProperty("classification") val classification: Classification,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @param:JsonProperty("additionalClassifications") @field:JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>?,

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
    }

    data class PlanningEiCreate @JsonCreator constructor(

        val budget: BudgetEiCreate,

        var rationale: String?
    ) {

        data class BudgetEiCreate @JsonCreator constructor(

            var period: Period
        )
    }
}