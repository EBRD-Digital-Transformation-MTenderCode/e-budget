package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiBudget @JsonCreator constructor(

        var id: String?,

        @field:Valid @field:NotNull
        var period: Period,

        @field:Valid
        var amount: EiValue?
)
