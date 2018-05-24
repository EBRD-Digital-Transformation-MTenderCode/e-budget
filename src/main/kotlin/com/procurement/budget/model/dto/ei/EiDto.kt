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
        @NotNull
        @JsonProperty("tender")
        var tender: EiTenderDto,

        @Valid
        @NotNull
        @JsonProperty("planning")
        var planning: EiPlanningDto,

        @Valid
        @NotNull
        @JsonProperty("buyer")
        val buyer: EiOrganizationReferenceDto
)
