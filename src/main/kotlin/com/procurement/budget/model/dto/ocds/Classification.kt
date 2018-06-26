package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class Classification @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:NotNull
        var scheme: Scheme,

        @field:NotNull
        val description: String,

        val uri: String?
)

