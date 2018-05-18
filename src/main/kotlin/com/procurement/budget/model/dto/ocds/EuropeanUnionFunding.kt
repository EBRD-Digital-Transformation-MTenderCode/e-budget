package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("projectName", "projectIdentifier", "uri")
data class EuropeanUnionFunding(

        @JsonProperty("projectName")
        val projectName: String,

        @JsonProperty("projectIdentifier")
        val projectIdentifier: String,

        @JsonProperty("uri")
        val uri: String?
)
