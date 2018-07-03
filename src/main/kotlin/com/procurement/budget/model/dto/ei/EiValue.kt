package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiValue @JsonCreator constructor(

        val amount: BigDecimal?,

        @field:NotNull
        var currency: Currency
)