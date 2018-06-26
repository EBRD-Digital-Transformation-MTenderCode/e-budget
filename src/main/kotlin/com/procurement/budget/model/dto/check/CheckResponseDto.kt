package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@JsonPropertyOrder("ei", "budgetBreakdown", "funder", "payer", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponseDto(

        @JsonProperty("ei")
        val ei: Set<String>?,

        @field:Valid @field:NotEmpty
        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        @field:Valid
        @JsonProperty("funder")
        val funder: HashSet<FsOrganizationReferenceDto>?,

        @field:Valid
        @JsonProperty("payer")
        val payer: HashSet<FsOrganizationReferenceDto>?,

        @field:Valid
        @JsonProperty("buyer")
        val buyer: HashSet<EiOrganizationReferenceDto>?
)
