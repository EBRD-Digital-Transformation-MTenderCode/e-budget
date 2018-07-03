package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EuropeanUnionFunding @JsonCreator constructor(

        @field:NotNull
        val projectName: String,

        @field:NotNull
        val projectIdentifier: String,

        val uri: String?
)
