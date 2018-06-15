package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsRequestUpdateBudgetOrganizationReferenceDto(

    @field:NotNull
    @JsonProperty("id")
    var id: String,

    @field:NotNull
    @JsonProperty("name")
    val name: String

)