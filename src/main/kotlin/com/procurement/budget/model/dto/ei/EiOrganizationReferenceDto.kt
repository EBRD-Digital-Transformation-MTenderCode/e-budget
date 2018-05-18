package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Details
import com.procurement.budget.model.dto.ocds.Identifier
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "id", "identifier", "address", "additionalIdentifiers", "contactPoint", "details", "buyerProfile")
data class EiOrganizationReferenceDto(

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("name")
        @Size(min = 1)
        val name: String,

        @Valid
        @JsonProperty("identifier")
        val identifier: Identifier,

        @Valid
        @JsonProperty("address")
        val address: Address,

        @Valid
        @JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: HashSet<Identifier>?,

        @Valid
        @JsonProperty("contactPoint")
        val contactPoint: ContactPoint,

        @Valid
        @JsonProperty("details")
        val details: Details,

        @JsonProperty("buyerProfile")
        val buyerProfile: String?
)
