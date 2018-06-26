package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CheckRequestDto @JsonCreator constructor(

        @field:Valid
        @field:NotEmpty
        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        @field:Valid
        @field:NotNull
        val tenderPeriod: CheckPeriodDto,

        @field:Valid
        @field:NotNull
        val classification: Classification
)
