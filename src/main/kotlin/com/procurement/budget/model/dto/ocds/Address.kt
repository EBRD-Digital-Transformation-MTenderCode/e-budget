package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address @JsonCreator constructor(

        @field:NotNull
        val streetAddress: String,

        val postalCode: String?,

        @field:NotNull
        val locality: String,

        @field:NotNull
        val region: String,

        @field:NotNull
        val countryName: String
)
