package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsTenderDto @JsonCreator constructor(

        val id: String?,

        val status: TenderStatus?,

        val statusDetails: TenderStatusDetails?,

        @field:Valid
        val procuringEntity: FsOrganizationReferenceDto?
)