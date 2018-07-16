package com.procurement.budget.model.dto.fs.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.fs.SourceEntityFs
import com.procurement.budget.model.dto.fs.ValueFs
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsUpdate @JsonCreator constructor(

//        @field:Valid @field:NotNull
//        var tender: TenderFsUpdate,

        @field:Valid @field:NotNull
        var planning: PlanningFsUpdate
)

//data class TenderFsUpdate @JsonCreator constructor(
//
//        @field:Valid
//        val procuringEntity: OrganizationReferenceFs?
//)

data class PlanningFsUpdate @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetFsUpdate,

        var rationale: String?
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

        val project: String?,

        val projectID: String?,

        val uri: String?
)