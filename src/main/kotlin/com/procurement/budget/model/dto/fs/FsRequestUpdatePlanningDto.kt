package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsRequestUpdatePlanningDto(

        @Valid
        @NotNull
        @JsonProperty("budget")
        val budget: FsRequestUpdateBudgetDto,

        @JsonProperty("rationale")
        val rationale: String?
)