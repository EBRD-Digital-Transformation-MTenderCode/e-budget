package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactPoint(

        @NotNull
        @JsonProperty("name")
        val name: String,

        @NotNull
        @JsonProperty("email")
        val email: String,

        @NotNull
        @JsonProperty("telephone")
        val telephone: String,

        @JsonProperty("faxNumber")
        val faxNumber: String,

        @NotNull
        @JsonProperty("url")
        val url: String
)

