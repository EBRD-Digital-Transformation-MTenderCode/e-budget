package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class Period @JsonCreator constructor(

        @field:NotNull
        val startDate: LocalDateTime,

        @field:NotNull
        val endDate: LocalDateTime
)
