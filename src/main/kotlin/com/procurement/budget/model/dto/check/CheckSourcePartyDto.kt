package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "name")
data class CheckSourcePartyDto(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("name")
        val name: String?
)
