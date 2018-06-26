package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsRequestUpdateDto  @JsonCreator constructor(

    @field:Valid
    @field:NotNull
    val ocid: String,

    @field:Valid
    @field:NotNull
    val planning: FsRequestUpdatePlanningDto,

    @field:Valid
    @field:NotNull
    val tender: FsTenderDto,

    @field:Valid
    val buyer: FsOrganizationReferenceDto?
)
