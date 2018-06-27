package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class FsRequestUpdateBudgetOrganizationReferenceDto @JsonCreator constructor(

    @field:NotNull
    var id: String,

    @field:NotNull
    val name: String

)