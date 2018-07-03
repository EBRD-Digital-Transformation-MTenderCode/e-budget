package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsRequestUpdateBudgetOrganizationReferenceDto @JsonCreator constructor(

        @field:NotNull
        var id: String,

        @field:NotNull
        val name: String

)