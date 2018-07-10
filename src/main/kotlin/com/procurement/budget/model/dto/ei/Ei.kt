package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Ei @JsonCreator constructor(

        var token: String?,

        var ocid: String?,

        @field:Valid @field:NotNull
        var tender: EiTender,

        @field:Valid @field:NotNull
        var planning: EiPlanning,

        @field:Valid @field:NotNull
        var buyer: EiOrganizationReference
)
