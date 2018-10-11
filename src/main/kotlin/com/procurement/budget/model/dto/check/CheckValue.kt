package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import java.math.BigDecimal

data class CheckValue @JsonCreator constructor(

        @field:JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal,

        val currency: String
)
