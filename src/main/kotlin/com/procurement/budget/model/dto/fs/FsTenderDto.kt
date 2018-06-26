package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsTenderDto @JsonCreator constructor(

        val id: String?,

        val status: TenderStatus?,

        val statusDetails: TenderStatusDetails?,

        @field:Valid
        val procuringEntity: FsOrganizationReferenceDto?
)