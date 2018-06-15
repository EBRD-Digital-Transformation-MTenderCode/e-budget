package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Classification(

        @field:NotNull
        @JsonProperty("id")
        val id: String,

        @field:NotNull
        @JsonProperty("scheme")
        var scheme: Scheme,

        @field:NotNull
        @JsonProperty("description")
        val description: String,

        @JsonProperty("uri")
        val uri: String?
)

