package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address(

        @NotNull
        @JsonProperty("streetAddress")
        val streetAddress: String,

        @NotNull
        @JsonProperty("locality")
        val locality: String,

        @NotNull
        @JsonProperty("region")
        val region: String,

        @JsonProperty("postalCode")
        val postalCode: String?,

        @NotNull
        @JsonProperty("countryName")
        val countryName: String
)
