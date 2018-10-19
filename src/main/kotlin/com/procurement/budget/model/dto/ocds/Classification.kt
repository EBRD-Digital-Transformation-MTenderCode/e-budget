package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification @JsonCreator constructor(

        val id: String,

        var scheme: Scheme,

        val description: String,

        val uri: String?
)

