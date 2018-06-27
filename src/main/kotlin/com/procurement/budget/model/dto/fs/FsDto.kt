package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsDto @JsonCreator constructor(

        var token: String?,

        val ocid: String?,

        @field:Valid
        @field:NotNull
        var tender: FsTenderDto,

        @field:Valid
        @field:NotNull
        var planning: FsPlanningDto,

        @field:Valid
        val funder: FsOrganizationReferenceDto?,

        @field:Valid
        val payer: FsOrganizationReferenceDto?
)
