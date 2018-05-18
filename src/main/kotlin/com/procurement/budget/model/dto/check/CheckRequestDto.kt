package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@JsonPropertyOrder("budgetBreakdown", "tenderPeriod", "classification")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckRequestDto(

        @Valid
        @NotEmpty
        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        @Valid
        @JsonProperty("tenderPeriod")
        val tenderPeriod: CheckPeriodDto,

        @Valid
        @JsonProperty("classification")
        val classification: Classification
)
