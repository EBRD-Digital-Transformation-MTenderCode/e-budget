package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
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

