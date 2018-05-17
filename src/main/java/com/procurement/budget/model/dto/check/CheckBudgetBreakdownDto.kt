package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "description", "amount", "period", "sourceParty")
data class CheckBudgetBreakdownDto(

        @JsonProperty("id")
        val id: String,

        @Valid
        @JsonProperty("description")
        val description: String?,

        @Valid
        @JsonProperty("amount")
        val amount: CheckValueDto,

        @Valid
        @JsonProperty("period")
        val period: Period?,

        @Valid
        @JsonProperty("sourceParty")
        val sourceParty: CheckSourcePartyDto?
)

