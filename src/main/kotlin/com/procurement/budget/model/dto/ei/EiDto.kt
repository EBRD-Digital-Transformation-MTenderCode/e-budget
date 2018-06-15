package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonPropertyOrder("token", "ocid", "tender", "planning", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class EiDto(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        var ocid: String?,

        @Valid
        @field:NotNull
        @JsonProperty("tender")
        var tender: EiTenderDto,

        @Valid
        @field:NotNull
        @JsonProperty("planning")
        var planning: EiPlanningDto,

        @Valid
        @field:NotNull
        @JsonProperty("buyer")
        var buyer: EiOrganizationReferenceDto
)
