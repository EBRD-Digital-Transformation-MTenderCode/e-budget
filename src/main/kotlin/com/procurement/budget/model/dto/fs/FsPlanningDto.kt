package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsPlanningDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val budget: FsBudgetDto,

        var rationale: String?
)
