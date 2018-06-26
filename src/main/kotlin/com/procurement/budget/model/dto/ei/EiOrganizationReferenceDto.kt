package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Details
import com.procurement.budget.model.dto.ocds.Identifier
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiOrganizationReferenceDto(

        @JsonProperty("id")
        var id: String?,

        @field:NotNull
        @Size(min = 1)
        @JsonProperty("name")
        val name: String,

        @field:Valid
        @field:NotNull
        @JsonProperty("identifier")
        val identifier: Identifier,

        @field:Valid
        @field:NotNull
        @JsonProperty("address")
        val address: Address,

        @field:Valid
        @JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: HashSet<Identifier>?,

        @field:Valid
        @field:NotNull
        @JsonProperty("contactPoint")
        val contactPoint: ContactPoint,

        @field:Valid
        @field:NotNull
        @JsonProperty("details")
        val details: Details,

        @JsonProperty("buyerProfile")
        val buyerProfile: String?
)
