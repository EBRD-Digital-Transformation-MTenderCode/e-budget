package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class CheckBudgetBreakdownDto @JsonCreator constructor(

        @field:NotNull
        val id: String,

        val description: String?,

        @field:Valid
        @field:NotNull
        val amount: CheckValueDto,

        @field:Valid
        var period: Period?,

        @field:Valid
        var sourceParty: CheckSourcePartyDto?
)

