package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal
import javax.validation.Valid

@JsonPropertyOrder("totalAmount", "fs")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsResponseDto(

        @field:Valid
        @JsonProperty("totalAmount")
        val totalAmount: BigDecimal,

        @field:Valid
        @JsonProperty("fs")
        val fs: FsDto
)
