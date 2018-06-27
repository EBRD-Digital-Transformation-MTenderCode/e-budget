package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotNull

data class FsRequestUpdateBudgetOrganizationReferenceDto @JsonCreator constructor(

        @field:NotNull
        var id: String,

        @field:NotNull
        val name: String

)