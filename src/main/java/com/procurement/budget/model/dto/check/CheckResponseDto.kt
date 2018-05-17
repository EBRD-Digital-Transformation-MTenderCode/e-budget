package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import java.util.*

@JsonPropertyOrder("ei", "budgetBreakdown", "funder", "payer", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponseDto(

        @JsonProperty("ei")
        val ei: Set<String>?,

        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>?,

        @JsonProperty("funder")
        val funder: HashSet<FsOrganizationReferenceDto>?,

        @JsonProperty("payer")
        val payer: HashSet<FsOrganizationReferenceDto>?,

        @JsonProperty("buyer")
        val buyer: HashSet<EiOrganizationReferenceDto>?
)
