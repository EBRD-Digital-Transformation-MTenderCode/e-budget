package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Identifier
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "name", "identifier", "address", "additionalIdentifiers", "contactPoint")
data class FsOrganizationReferenceDto(

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("name")
        @Size(min = 1)
        @NotNull
        val name: String,

        @Valid
        @NotNull
        @JsonProperty("identifier")
        val identifier: Identifier?,

        @Valid
        @NotNull
        @JsonProperty("address")
        val address: Address?,

        @Valid
        @JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: HashSet<Identifier>?,

        @Valid
        @NotNull
        @JsonProperty("contactPoint")
        val contactPoint: ContactPoint?
)