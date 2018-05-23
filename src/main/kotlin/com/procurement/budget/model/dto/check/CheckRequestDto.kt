package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckRequestDto(

        @Valid
        @NotEmpty
        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        @Valid
        @NotNull
        @JsonProperty("tenderPeriod")
        val tenderPeriod: CheckPeriodDto,

        @Valid
        @NotNull
        @JsonProperty("classification")
        val classification: Classification
)
