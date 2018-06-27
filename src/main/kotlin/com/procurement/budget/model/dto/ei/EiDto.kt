package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class EiDto @JsonCreator constructor(

        var token: String?,

        var ocid: String?,

        @field:Valid
        @field:NotNull
        var tender: EiTenderDto,

        @field:Valid
        @field:NotNull
        var planning: EiPlanningDto,

        @field:Valid
        @field:NotNull
        var buyer: EiOrganizationReferenceDto
)
