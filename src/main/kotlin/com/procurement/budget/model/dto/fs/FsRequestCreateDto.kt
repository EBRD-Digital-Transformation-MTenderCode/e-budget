package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsRequestCreateDto @JsonCreator constructor(

        @field:Valid        @field:NotNull
        val planning: FsPlanningDto,

        @field:Valid        @field:NotNull
        val tender: FsTenderDto,

        @field:Valid
        val buyer: FsOrganizationReferenceDto?
)
