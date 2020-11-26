package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Identifier
import com.procurement.budget.model.dto.ocds.Period
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.model.dto.ocds.Value

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

        val amount: Value,

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

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceFs @JsonCreator constructor(

        var id: String?,

        val name: String,

        val identifier: Identifier,

        val address: Address,

        val additionalIdentifiers: List<Identifier>?,

        val contactPoint: ContactPoint
)

data class SourceEntityFs @JsonCreator constructor(

        var id: String,

        val name: String
)