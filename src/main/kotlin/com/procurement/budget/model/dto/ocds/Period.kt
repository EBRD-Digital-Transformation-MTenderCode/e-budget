package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.budget.model.dto.databinding.JsonDateDeserializer
import com.procurement.budget.model.dto.databinding.JsonDateSerializer
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class Period @JsonCreator constructor(

        @field:NotNull
        val startDate: LocalDateTime,

        @field:NotNull
        val endDate: LocalDateTime
)
