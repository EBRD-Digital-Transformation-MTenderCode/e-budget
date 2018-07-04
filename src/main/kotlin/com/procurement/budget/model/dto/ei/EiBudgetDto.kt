package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiBudgetDto @JsonCreator constructor(

        var id: String?,

        @field:Valid @field:NotNull
        val period: Period,

        @field:Valid @field:NotNull
        val amount: EiValue
)
