package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckBudgetBreakdownDto(

        @NotNull
        @JsonProperty("id")
        val id: String,

        @JsonProperty("description")
        val description: String?,

        @Valid
        @NotNull
        @JsonProperty("amount")
        val amount: CheckValueDto,

        @Valid
        //TODO Notnull
        @JsonProperty("period")
        var period: Period?,

        @Valid
        //TODO notnull
        @JsonProperty("sourceParty")
        var sourceParty: CheckSourcePartyDto?
)

