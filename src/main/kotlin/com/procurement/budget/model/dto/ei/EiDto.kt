package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonPropertyOrder("token", "ocid", "tender", "planning", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class EiDto(

        @JsonProperty("token")
        var token: String?,

        @JsonProperty("ocid")
        var ocId: String?,

        @Valid
        @JsonProperty("tender")
        var tender: EiTenderDto,

        @Valid
        @JsonProperty("planning")
        var planning: EiPlanningDto,

        @Valid
        @JsonProperty("buyer")
        val buyer: EiOrganizationReferenceDto
)
