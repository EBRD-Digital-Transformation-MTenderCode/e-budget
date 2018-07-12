package com.procurement.budget.model.dto.fs.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.fs.OrganizationReferenceSourceEntityFs
import com.procurement.budget.model.dto.fs.ValueFs
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsUpdate @JsonCreator constructor(


        @field:Valid @field:NotNull
        var planning: PlanningFsUpdate,

        @field:Valid @field:NotNull
        var tender: TenderFsUpdate,

        @field:Valid
        val buyer: OrganizationReferenceFs?
)


data class PlanningFsUpdate @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetFsUpdate,

        var rationale: String?
)

data class TenderFsUpdate @JsonCreator constructor(

        val id: String,

        @field:Valid
        val procuringEntity: OrganizationReferenceFs?
)

data class BudgetFsUpdate @JsonCreator constructor(

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
        var sourceEntity: OrganizationReferenceSourceEntityFs,


        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("verified")
        var verified: Boolean?
)