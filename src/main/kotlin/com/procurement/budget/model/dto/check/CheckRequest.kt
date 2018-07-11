package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CheckRequest @JsonCreator constructor(

        @field:Valid @field:NotEmpty
        val budgetBreakdown: List<CheckBudgetBreakdown>,

        @field:Valid @field:NotNull
        val tenderPeriod: CheckPeriod,

        @field:Valid @field:NotNull
        val classification: Classification
)
