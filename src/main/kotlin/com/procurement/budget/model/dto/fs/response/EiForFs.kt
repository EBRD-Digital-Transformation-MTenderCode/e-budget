package com.procurement.budget.model.dto.fs.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.ei.ValueEi

data class EiForFs @JsonCreator constructor(

        var planning: EiForFsPlanning
)

data class EiForFsPlanning @JsonCreator constructor(

        var budget: EiForFsBudget
)

data class EiForFsBudget @JsonCreator constructor(

        var amount: ValueEi?
)
