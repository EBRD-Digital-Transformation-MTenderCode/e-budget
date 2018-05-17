package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonPropertyOrder("token", "ocid", "tender", "planning", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class EiDto(

        @JsonProperty("token")
        val token: String?,

        @JsonProperty("ocid")
        val ocId: String?,

        @Valid
        @JsonProperty("tender")
        val tender: EiTenderDto,

        @Valid
        @JsonProperty("planning")
        val planning: EiPlanningDto,

        @Valid
        @JsonProperty("buyer")
        val buyer: EiOrganizationReferenceDto
)
