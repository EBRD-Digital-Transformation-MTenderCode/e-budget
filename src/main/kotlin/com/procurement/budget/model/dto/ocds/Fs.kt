package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.create.FsPlanningCreateDto
import com.procurement.budget.model.dto.fs.create.FsTenderCreateDto
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class Fs(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        val ocid: String?,

        @Valid
        @NotNull
        @JsonProperty("tender")
        var tender: FsTenderCreateDto,

        @Valid
        @NotNull
        @JsonProperty("planning")
        var planning: FsPlanningCreateDto,

        @Valid
        @JsonProperty("funder")
        val funder: FsOrganizationReferenceDto?,

        @Valid
        @JsonProperty("payer")
        val payer: FsOrganizationReferenceDto?
)
