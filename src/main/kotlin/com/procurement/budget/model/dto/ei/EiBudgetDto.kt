package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "period", "amount")
data class EiBudgetDto(

        @JsonProperty("id")
        var id: String?,

        @Valid
        @field:NotNull
        @JsonProperty("period")
        val period: Period,

        @Valid
        @field:NotNull
        @JsonProperty("amount")
        val amount: EiValue
)
