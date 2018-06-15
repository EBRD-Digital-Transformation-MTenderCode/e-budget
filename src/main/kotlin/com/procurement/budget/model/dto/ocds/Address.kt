package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address(

        @field:NotNull
        @JsonProperty("streetAddress")
        val streetAddress: String,

        @field:NotNull
        @JsonProperty("locality")
        val locality: String,

        @field:NotNull
        @JsonProperty("region")
        val region: String,

        @JsonProperty("postalCode")
        val postalCode: String?,

        @field:NotNull
        @JsonProperty("countryName")
        val countryName: String
)
