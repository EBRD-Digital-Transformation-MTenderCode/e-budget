package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.Period

data class EiCreate @JsonCreator constructor(

        var tender: TenderEiCreate,

        var planning: PlanningEiCreate,

        var buyer: OrganizationReferenceEi
)

data class TenderEiCreate @JsonCreator constructor(

        var title: String,

        var description: String?,

        val classification: Classification,

        val mainProcurementCategory: String
)

data class PlanningEiCreate @JsonCreator constructor(

        val budget: BudgetEiCreate,

        var rationale: String?
)

data class BudgetEiCreate @JsonCreator constructor(

        var period: Period
)
