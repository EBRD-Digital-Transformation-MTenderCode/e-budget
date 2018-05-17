package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "scheme", "legalName", "uri")
data class Identifier(

        @param:JsonProperty("id")
        val id: String,

        @param:JsonProperty("scheme")
        val scheme: String,

        @param:JsonProperty("legalName")
        val legalName: String,

        @param:JsonProperty("uri")
        val uri: String
)
