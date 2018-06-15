package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Identifier(

        @field:NotNull
        @JsonProperty("id")
        val id: String,

        @field:NotNull
        @JsonProperty("scheme")
        val scheme: String,

        @field:NotNull
        @JsonProperty("legalName")
        val legalName: String,

        @field:NotNull
        @JsonProperty("uri")
        val uri: String
)
