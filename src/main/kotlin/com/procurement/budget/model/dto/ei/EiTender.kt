package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiTender @JsonCreator constructor(

        var id: String?,

        @field:NotNull
        var title: String,

        var description: String?,

        var status: TenderStatus?,

        var statusDetails: TenderStatusDetails?,

        @field:Valid @field:NotNull
        val classification: Classification,

        @field:NotNull
        val mainProcurementCategory: String?
)
