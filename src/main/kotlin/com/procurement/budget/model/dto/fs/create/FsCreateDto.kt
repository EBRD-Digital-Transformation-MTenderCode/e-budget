package com.procurement.budget.model.dto.fs.create

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsCreateDto(

        @Valid
        @NotNull
        @JsonProperty("planning")
        val planning: FsPlanningCreateDto,

        @Valid
        @NotNull
        @JsonProperty("tender")
        val tender: FsTenderCreateDto,

        @Valid
        @JsonProperty("buyer")
        val buyer: FsOrganizationReferenceDto?
)
