package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("budget", "rationale")
data class FsPlanningDto(

        @Valid
        @JsonProperty("budget")
        val budget: FsBudgetDto,

        @JsonProperty("rationale")
        val rationale: String?
)
