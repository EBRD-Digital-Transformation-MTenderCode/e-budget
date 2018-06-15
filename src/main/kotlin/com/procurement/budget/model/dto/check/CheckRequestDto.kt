package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Classification
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckRequestDto(

        @field:NotNull
        @JsonProperty("isEuropeanUnionFunded")
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean,

        @Valid
        @NotEmpty
        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        @Valid
        @field:NotNull
        @JsonProperty("tenderPeriod")
        val tenderPeriod: CheckPeriodDto,

        @Valid
        @field:NotNull
        @JsonProperty("classification")
        val classification: Classification
)
