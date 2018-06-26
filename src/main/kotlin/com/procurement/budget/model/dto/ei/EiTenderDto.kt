package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiTenderDto(

        @JsonProperty("id")
        var id: String?,

        @field:NotNull
        @JsonProperty("title")
        val title: String,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        var status: TenderStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: TenderStatusDetails?,

        @field:Valid
        @field:NotNull
        @JsonProperty("classification")
        val classification: Classification
)
