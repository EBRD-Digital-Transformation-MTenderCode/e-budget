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

        var tender: TenderFs,

        var planning: PlanningFs,

        val funder: OrganizationReferenceFs?,

        val payer: OrganizationReferenceFs
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderFs @JsonCreator constructor(

        val id: String,

        val status: TenderStatus,

        val statusDetails: TenderStatusDetails,

        val procuringEntity: OrganizationReferenceFs?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlanningFs @JsonCreator constructor(

        val budget: BudgetFs,

        var rationale: String?
)

data class BudgetFs @JsonCreator constructor(

        var id: String?,

        var description: String?,

        var period: Period,

        val amount: ValueFs,

        var europeanUnionFunding: EuropeanUnionFunding?,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        var isEuropeanUnionFunded: Boolean,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("verified")
        var verified: Boolean?,

        var sourceEntity: SourceEntityFs,

        var verificationDetails: String?,

        var project: String?,

        var projectID: String?,

        var uri: String?
)

data class ValueFs @JsonCreator constructor(

        @field:JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        val currency: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceFs @JsonCreator constructor(

        var id: String?,

        val name: String,

        val identifier: Identifier,

        val address: Address,

        val additionalIdentifiers: HashSet<Identifier>?,

        val contactPoint: ContactPoint?
)

data class SourceEntityFs @JsonCreator constructor(

        var id: String,

        val name: String
)