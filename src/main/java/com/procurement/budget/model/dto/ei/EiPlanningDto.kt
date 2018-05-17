package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("budget", "rationale")
data class EiPlanningDto(

        @Valid
        @JsonProperty("budget")
        val budget: EiBudgetDto,

        @JsonProperty("rationale")
        val rationale: String?
)
