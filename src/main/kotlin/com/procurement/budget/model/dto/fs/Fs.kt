package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Fs @JsonCreator constructor(

        var token: String?,

        val ocid: String?,

        @field:Valid @field:NotNull
        var tender: FsTender,

        @field:Valid @field:NotNull
        var planning: FsPlanning,

        @field:Valid
        val buyer: FsOrganizationReference?,

        @field:Valid
        val funder: FsOrganizationReference?,

        @field:Valid
        val payer: FsOrganizationReference?
)
