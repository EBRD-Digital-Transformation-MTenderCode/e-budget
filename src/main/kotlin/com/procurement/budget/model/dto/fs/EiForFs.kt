package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ei.EiPlanning
import com.procurement.budget.model.dto.ei.EiValue
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiForFs @JsonCreator constructor(

        var planning: EiForFsPlanning
)

data class EiForFsPlanning @JsonCreator constructor(

        var budget: EiForFsBudget
)

data class EiForFsBudget @JsonCreator constructor(

        var amount: EiValue?
)
