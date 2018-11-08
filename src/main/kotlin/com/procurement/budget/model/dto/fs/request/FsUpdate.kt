package com.procurement.budget.model.dto.fs.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import com.procurement.budget.model.dto.ocds.Value

data class FsUpdate @JsonCreator constructor(

        var planning: PlanningFsUpdate
)

data class PlanningFsUpdate @JsonCreator constructor(

        val budget: BudgetFsUpdate,

        var rationale: String?
)

data class BudgetFsUpdate @JsonCreator constructor(

        var id: String?,

        var description: String?,

        val period: Period,

        val amount: Value,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean,

        var europeanUnionFunding: EuropeanUnionFunding?,

        val project: String?,

        val projectID: String?,

        val uri: String?
)