package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiBudgetDto  @JsonCreator constructor(

        var id: String?,

        @field:Valid
        @field:NotNull
        val period: Period,

        @field:Valid
        @field:NotNull
        val amount: EiValue
)
