package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class CheckPeriodDto @JsonCreator constructor(

        @field:NotNull
        val startDate: LocalDateTime
)
