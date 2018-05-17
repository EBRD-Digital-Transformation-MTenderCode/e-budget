package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.ocds.Currency
import com.procurement.notice.databinding.MoneyDeserializer
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("amount", "currency")
data class CheckValueDto(

        @JsonProperty("amount")
        @JsonDeserialize(using = MoneyDeserializer::class)
        private val amount: BigDecimal,

        @JsonProperty("currency")
        private val currency: Currency
)
