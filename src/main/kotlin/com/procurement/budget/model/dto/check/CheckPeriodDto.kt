package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.budget.model.dto.databinding.JsonDateDeserializer
import com.procurement.budget.model.dto.databinding.JsonDateSerializer
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class CheckPeriodDto @JsonCreator constructor(

        @field:NotNull
        @field:JsonDeserialize(using = JsonDateDeserializer::class)
        @field:JsonSerialize(using = JsonDateSerializer::class)
        val startDate: LocalDateTime
)
