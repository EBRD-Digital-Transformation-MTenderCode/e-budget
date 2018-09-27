package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotNull

data class EiUpdate @JsonCreator constructor(

        var planning: PlanningEiUpdate?,

        var tender: TenderEiUpdate

)

data class PlanningEiUpdate @JsonCreator constructor(

        var rationale: String?
)

data class TenderEiUpdate @JsonCreator constructor(

        var title: String,

        var description: String?
)
