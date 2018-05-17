package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "scheme", "description", "uri")
data class Classification(

        @JsonProperty("scheme")
        val scheme: Scheme,

        @JsonProperty("id")
        val id: String,

        @JsonProperty("description")
        val description: String,

        @JsonProperty("uri")
        val uri: String?
)

