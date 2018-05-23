package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification(

        @NotNull
        @JsonProperty("id")
        val id: String,

        @NotNull
        @JsonProperty("scheme")
        val scheme: Scheme,

        @NotNull
        @JsonProperty("description")
        val description: String,

        @JsonProperty("uri")
        val uri: String?
)

