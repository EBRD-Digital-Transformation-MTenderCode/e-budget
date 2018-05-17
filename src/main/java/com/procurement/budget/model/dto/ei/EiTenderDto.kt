package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "description", "status", "statusDetails", "classification")
data class EiTenderDto(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        val status: TenderStatus?,

        @JsonProperty("statusDetails")
        val statusDetails: TenderStatusDetails?,

        @Valid
        @JsonProperty("classification")
        private val classification: Classification
)
