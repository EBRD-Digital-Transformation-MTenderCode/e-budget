package com.procurement.budget.model.dto.fs.update

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsUpdateDto(

        @Valid
        @NotNull
        @JsonProperty("planning")
        val planning: FsPlanningUpdateDto,

        @Valid
        @NotNull
        @JsonProperty("tender")
        val tender: FsTenderUpdateDto,

        @Valid
        @JsonProperty("buyer")
        val buyer: FsOrganizationReferenceDto?
)
