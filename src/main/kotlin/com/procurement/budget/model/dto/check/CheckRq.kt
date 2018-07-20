package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CheckRq @JsonCreator constructor(

        @field:Valid @field:NotNull
        var planning: PlanningCheckRq,

        @field:Valid @field:NotNull
        var tender: TenderCheckRq
)


data class PlanningCheckRq @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetCheckRq,

        var rationale: String?
)

data class BudgetCheckRq @JsonCreator constructor(

        var description: String?,

        @field:Valid @field:NotEmpty
        val budgetBreakdown: List<BudgetBreakdownCheckRq>
)

data class BudgetBreakdownCheckRq @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:Valid
        @field:NotNull
        val amount: CheckValue
)

data class TenderCheckRq @JsonCreator constructor(

        @field:Valid @field:NotNull
        val classification: Classification
)
