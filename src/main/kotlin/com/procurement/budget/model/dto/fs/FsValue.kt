package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsValue(

        @field:NotNull
        @JsonProperty("amount")
        @JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        @field:NotNull
        @JsonProperty("currency")
        val currency: Currency
)