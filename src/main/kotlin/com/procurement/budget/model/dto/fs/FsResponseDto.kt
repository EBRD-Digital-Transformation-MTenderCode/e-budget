package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigDecimal

data class FsResponseDto @JsonCreator constructor(

        val totalAmount: BigDecimal,

        val fs: FsDto
)
