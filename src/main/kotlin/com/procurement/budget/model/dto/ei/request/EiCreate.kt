package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ei.EiOrganizationReference
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiCreate @JsonCreator constructor(

        var token: String?,

        var ocid: String?,

        @field:Valid @field:NotNull
        var tender: EiCreateTender,

        @field:Valid @field:NotNull
        var planning: EiCreatePlanning,

        @field:Valid @field:NotNull
        var buyer: EiOrganizationReference
)

data class EiCreateTender @JsonCreator constructor(

        @field:NotNull
        var title: String,

        var description: String?,

        @field:Valid @field:NotNull
        val classification: Classification,

        @field:NotNull
        val mainProcurementCategory: String?
)

data class EiCreatePlanning @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: EiCreateBudget,

        var rationale: String?
)

data class EiCreateBudget @JsonCreator constructor(

        @field:Valid @field:NotNull
        var period: Period
)