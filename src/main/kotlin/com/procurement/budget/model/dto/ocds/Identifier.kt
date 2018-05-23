package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Identifier(

        @NotNull
        @JsonProperty("id")
        val id: String,

        @NotNull
        @JsonProperty("scheme")
        val scheme: String,

        @NotNull
        @JsonProperty("legalName")
        val legalName: String,

        @NotNull
        @JsonProperty("uri")
        val uri: String
)
