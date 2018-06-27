package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsRequestCreateDto @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val planning: FsPlanningDto,

        @field:Valid
        @field:NotNull
        val tender: FsTenderDto,

        @field:Valid
        val buyer: FsOrganizationReferenceDto?
)
