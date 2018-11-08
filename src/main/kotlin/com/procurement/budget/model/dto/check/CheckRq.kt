package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ocds.Classification

data class CheckRq @JsonCreator constructor(

        var planning: PlanningCheckRq,

        var tender: TenderCheckRq
)


data class PlanningCheckRq @JsonCreator constructor(

        val budget: BudgetCheckRq,

        var rationale: String?
)

data class BudgetCheckRq @JsonCreator constructor(

        var description: String?,

        val budgetBreakdown: List<BudgetBreakdownCheckRq>
)

data class BudgetBreakdownCheckRq @JsonCreator constructor(

        val id: String,

        val amount: CheckValue
)

data class TenderCheckRq @JsonCreator constructor(

        var classification: Classification?
)
