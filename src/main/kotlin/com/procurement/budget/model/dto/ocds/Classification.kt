package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification @JsonCreator constructor(

        @field:NotNull
        val id: String,

        @field:NotNull
        var scheme: Scheme,

        @field:NotNull
        val description: String,

        val uri: String?
)

