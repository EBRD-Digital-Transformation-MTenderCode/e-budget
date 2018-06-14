package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsTenderDto(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("status")
        val status: TenderStatus?,

        @JsonProperty("statusDetails")
        val statusDetails: TenderStatusDetails?,

        @Valid
        @JsonProperty("procuringEntity")
        val procuringEntity: FsOrganizationReferenceDto?
)