package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiPlanningDto(

        @Valid
        @NotNull
        @JsonProperty("budget")
        val budget: EiBudgetDto,

        @JsonProperty("rationale")
        val rationale: String?
)
