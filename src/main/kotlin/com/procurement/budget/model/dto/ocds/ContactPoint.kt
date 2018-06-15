package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactPoint(

        @field:NotNull
        @JsonProperty("name")
        val name: String,

        @field:NotNull
        @JsonProperty("email")
        val email: String,

        @field:NotNull
        @JsonProperty("telephone")
        val telephone: String,

        @JsonProperty("faxNumber")
        val faxNumber: String?,

        @field:NotNull
        @JsonProperty("url")
        val url: String
)

