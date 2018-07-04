package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotNull

data class CheckValueDto @JsonCreator constructor(

        @field:NotNull
        @field:JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal,

        @field:NotNull
        val currency: Currency
)
