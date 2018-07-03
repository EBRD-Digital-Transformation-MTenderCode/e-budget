package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Details
import com.procurement.budget.model.dto.ocds.Identifier
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiOrganizationReferenceDto @JsonCreator constructor(

        var id: String?,

        @field:NotNull
        val name: String,

        @field:Valid        @field:NotNull
        val identifier: Identifier,

        @field:Valid        @field:NotNull
        val address: Address,

        @field:Valid
        val additionalIdentifiers: HashSet<Identifier>?,

        @field:Valid        @field:NotNull
        val contactPoint: ContactPoint,

        @field:Valid        @field:NotNull
        val details: Details,

        val buyerProfile: String?
)
