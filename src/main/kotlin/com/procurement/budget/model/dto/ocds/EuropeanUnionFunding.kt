package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EuropeanUnionFunding(

        @NotNull
        @JsonProperty("projectName")
        val projectName: String,

        @NotNull
        @JsonProperty("projectIdentifier")
        val projectIdentifier: String,

        @JsonProperty("uri")
        val uri: String?
)
