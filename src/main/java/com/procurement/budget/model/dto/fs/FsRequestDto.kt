package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonPropertyOrder("tender", "planning", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsRequestDto(

        @Valid
        @JsonProperty("tender")
        val tender: FsTenderDto,

        @Valid
        @JsonProperty("planning")
        val planning: FsPlanningDto,

        @Valid
        @JsonProperty("buyer")
        val buyer: FsOrganizationReferenceDto?
)
