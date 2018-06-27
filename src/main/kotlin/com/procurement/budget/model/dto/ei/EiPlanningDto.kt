package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiPlanningDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val budget: EiBudgetDto,

        val rationale: String?
)
