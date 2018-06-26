package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class EiDto @JsonCreator constructor(

        var token: String?,

        var ocid: String?,

        @field:Valid
        @field:NotNull
        @JsonProperty("tender")
        var tender: EiTenderDto,

        @field:Valid
        @field:NotNull
        @JsonProperty("planning")
        var planning: EiPlanningDto,

        @field:Valid
        @field:NotNull
        @JsonProperty("buyer")
        var buyer: EiOrganizationReferenceDto
)
