package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class EuropeanUnionFunding @JsonCreator constructor(

        @field:NotNull
        val projectName: String,

        @field:NotNull
        val projectIdentifier: String,

        val uri: String?
)
