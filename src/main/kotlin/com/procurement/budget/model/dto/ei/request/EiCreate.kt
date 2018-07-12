package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiCreate @JsonCreator constructor(

        @field:Valid @field:NotNull
        var planning: PlanningEiCreate,

        @field:Valid @field:NotNull
        var tender: TenderEiCreate,

        @field:Valid @field:NotNull
        var buyer: OrganizationReferenceEi
)

data class PlanningEiCreate @JsonCreator constructor(

        @field:Valid @field:NotNull
        val budget: BudgetEiCreate,

        var rationale: String?
)

data class BudgetEiCreate @JsonCreator constructor(

        @field:Valid @field:NotNull
        var period: Period
)

data class TenderEiCreate @JsonCreator constructor(

        @field:NotNull
        var title: String,

        var description: String?,

        @field:Valid @field:NotNull
        val classification: Classification,

        @field:NotNull
        val mainProcurementCategory: String
)