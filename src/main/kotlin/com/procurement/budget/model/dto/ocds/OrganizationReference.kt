package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Identifier
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReference(

        @JsonProperty("id")
        var id: String,


        @Size(min = 1)
        @JsonProperty("name")
        val name: String,

        @Valid
        @NotNull
        @JsonProperty("identifier")
        val identifier: Identifier,

        @Valid
        @NotNull
        @JsonProperty("address")
        val address: Address,

        @Valid
        @JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: HashSet<Identifier>?,

        @Valid
        @NotNull
        @JsonProperty("contactPoint")
        val contactPoint: ContactPoint
)