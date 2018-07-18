package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Address @JsonCreator constructor(

        @field:NotNull
        val streetAddress: String,

        val postalCode: String?,

        @field:Valid @field:NotNull
        val addressDetails: AddressDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AddressDetails(

        @field:Valid @field:NotNull
        val country: Country,

        @field:Valid @field:NotNull
        val region: Region,

        @field:Valid @field:NotNull
        val locality: Locality
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(

        val scheme: String?,

        @field:NotNull
        val id: String,

        val description: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Region(

        val scheme: String?,

        @field:NotNull
        val id: String,

        val description: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Locality(

        @field:NotNull
        val scheme: String,

        @field:NotNull
        val id: String,

        @field:NotNull
        val description: String,

        val uri: String?
)