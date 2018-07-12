package com.procurement.budget.model.dto.fs.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.fs.ValueFs
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsCreate @JsonCreator constructor(


        @field:Valid @field:NotNull
        var planning: PlanningFsCreate,

        @field:Valid @field:NotNull
        var tender: TenderFsCreate,

        @field:Valid
        val buyer: OrganizationReferenceFs?,

        @field:Valid
        val funder: OrganizationReferenceFs?,

        @field:Valid
        val payer: OrganizationReferenceFs?
)


data class TenderFsCreate @JsonCreator constructor(

        @field:Valid @field:NotNull
        val procuringEntity: OrganizationReferenceFs
)

data class PlanningFsCreate @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetFsCreate,

        var rationale: String?
)

data class BudgetFsCreate @JsonCreator constructor(

        var id: String?,

        var description: String?,

        @field:Valid @field:NotNull
        val period: Period,

        @field:Valid @field:NotNull
        val amount: ValueFs,

        @field:NotNull
        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @field:Valid
        var europeanUnionFunding: EuropeanUnionFunding?,

        @field:Valid
        var sourceEntity: OrganizationReferenceFs?
)

