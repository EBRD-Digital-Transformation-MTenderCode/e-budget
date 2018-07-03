package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsPlanningDto @JsonCreator constructor(

        @field:Valid        @field:NotNull
        val budget: FsBudgetDto,

        var rationale: String?
)
