package com.procurement.budget.model.dto.ei.request

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotNull

data class EiUpdate @JsonCreator constructor(

        var tender: EiUpdateTender,

        var planning: EiUpdatePlanning?
)

data class EiUpdateTender @JsonCreator constructor(

        @field:NotNull
        var title: String,

        var description: String?
)

data class EiUpdatePlanning @JsonCreator constructor(

        var rationale: String?
)
