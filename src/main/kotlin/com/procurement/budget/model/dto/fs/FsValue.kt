package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotNull

data class FsValue @JsonCreator constructor(

        @field:NotNull
        var amount: BigDecimal,

        @field:NotNull
        val currency: Currency
)