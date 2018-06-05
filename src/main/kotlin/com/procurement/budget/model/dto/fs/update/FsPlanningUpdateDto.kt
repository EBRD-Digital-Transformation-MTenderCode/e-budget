package com.procurement.budget.model.dto.fs.update

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsPlanningUpdateDto(

        @Valid
        @NotNull
        @JsonProperty("budget")
        val budget: FsBudgetUpdateDto,

        @JsonProperty("rationale")
        val rationale: String?
)
