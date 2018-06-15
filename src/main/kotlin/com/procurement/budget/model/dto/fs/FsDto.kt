package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsDto(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        val ocid: String?,

        @Valid
        @field:NotNull
        @JsonProperty("tender")
        var tender: FsTenderDto,

        @Valid
        @field:NotNull
        @JsonProperty("planning")
        var planning: FsPlanningDto,

        @Valid
        @JsonProperty("funder")
        val funder: FsOrganizationReferenceDto?,

        @Valid
        @JsonProperty("payer")
        val payer: FsOrganizationReferenceDto?
)
