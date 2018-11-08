package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import java.math.BigDecimal

data class Value @JsonCreator constructor(

        @JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        val currency: String
)