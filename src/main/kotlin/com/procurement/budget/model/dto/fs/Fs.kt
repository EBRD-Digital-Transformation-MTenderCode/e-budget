package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.*
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Fs @JsonCreator constructor(

        var token: String? = null,

        val ocid: String,

        @field:Valid @field:NotNull
        var tender: TenderFs,

        @field:Valid @field:NotNull
        var planning: PlanningFs,

        @field:Valid
        val buyer: OrganizationReferenceFs?,

        @field:Valid
        val funder: OrganizationReferenceFs?,

        @field:Valid
        val payer: OrganizationReferenceFs
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderFs @JsonCreator constructor(

        val id: String,

        val status: TenderStatus,

        val statusDetails: TenderStatusDetails,

        @field:Valid
        val procuringEntity: OrganizationReferenceFs?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlanningFs @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetFs,

        var rationale: String?
)

data class BudgetFs @JsonCreator constructor(

        var id: String,

        var description: String?,

        @field:Valid @field:NotNull
        val period: Period,

        @field:Valid @field:NotNull
        val amount: ValueFs,

        @field:Valid
        var europeanUnionFunding: EuropeanUnionFunding?,

        @field:NotNull
        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("verified")
        var verified: Boolean?,

        @field:Valid
        var sourceEntity: OrganizationReferenceFs?,

        var verificationDetails: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValueFs @JsonCreator constructor(

        @field:NotNull
        @field:JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        @field:NotNull
        val currency: Currency
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceFs @JsonCreator constructor(

        var id: String?,

        @field:NotNull
        val name: String,

        @field:Valid @field:NotNull
        val identifier: Identifier?,

        @field:Valid @field:NotNull
        val address: Address?,

        @field:Valid
        val additionalIdentifiers: HashSet<Identifier>?,

        @field:Valid @field:NotNull
        val contactPoint: ContactPoint?
)