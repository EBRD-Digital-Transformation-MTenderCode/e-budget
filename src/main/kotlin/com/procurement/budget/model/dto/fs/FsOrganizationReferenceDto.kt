package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.Address
import com.procurement.budget.model.dto.ocds.ContactPoint
import com.procurement.budget.model.dto.ocds.Identifier
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class FsOrganizationReferenceDto @JsonCreator constructor(

        var id: String?,

        @field:NotNull
        val name: String,

        @field:Valid
        @field:NotNull
        val identifier: Identifier?,

        @field:Valid
        @field:NotNull
        val address: Address?,

        @field:Valid
        val additionalIdentifiers: HashSet<Identifier>?,

        @field:Valid
        @field:NotNull
        val contactPoint: ContactPoint?
)