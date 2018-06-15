package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckValueDto(

        @field:NotNull
        @JsonProperty("amount")
        @JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal,

        @field:NotNull
        @JsonProperty("currency")
        val currency: Currency
)
