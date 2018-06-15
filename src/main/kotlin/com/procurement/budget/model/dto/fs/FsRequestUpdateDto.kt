package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class FsRequestUpdateDto(

    @Valid
    @field:NotNull
    @JsonProperty("ocid")
    val ocid: String,

    @Valid
    @field:NotNull
    @JsonProperty("planning")
    val planning: FsRequestUpdatePlanningDto,

    @Valid
    @field:NotNull
    @JsonProperty("tender")
    val tender: FsTenderDto,

    @Valid
    @JsonProperty("buyer")
    val buyer: FsOrganizationReferenceDto?
)
