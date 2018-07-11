package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class CheckPeriod @JsonCreator constructor(

        @field:NotNull
        val startDate: LocalDateTime
)
