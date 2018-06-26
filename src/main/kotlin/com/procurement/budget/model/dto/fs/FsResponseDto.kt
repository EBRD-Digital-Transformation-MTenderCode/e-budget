package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal
import javax.validation.Valid

data class FsResponseDto  @JsonCreator constructor(

        val totalAmount: BigDecimal,

        val fs: FsDto
)
