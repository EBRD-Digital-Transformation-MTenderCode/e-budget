package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonPropertyOrder("token", "ocid", "tender", "planning", "funder", "payer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsDto(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        val ocid: String?,

        @Valid
        @JsonProperty("tender")
        var tender: FsTenderDto,

        @Valid
        @JsonProperty("planning")
        var planning: FsPlanningDto,

        @Valid
        @JsonProperty("funder")
        val funder: FsOrganizationReferenceDto?,

        @Valid
        @JsonProperty("payer")
        val payer: FsOrganizationReferenceDto?
)
