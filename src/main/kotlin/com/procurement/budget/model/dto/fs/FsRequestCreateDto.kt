package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsRequestCreateDto(

        @field:Valid
        @field:NotNull
        @JsonProperty("planning")
        val planning: FsPlanningDto,

        @field:Valid
        @field:NotNull
        @JsonProperty("tender")
        val tender: FsTenderDto,

        @field:Valid
        @JsonProperty("buyer")
        val buyer: FsOrganizationReferenceDto?
)
