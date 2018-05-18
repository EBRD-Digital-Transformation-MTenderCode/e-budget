package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import java.util.*
import javax.validation.Valid

@JsonPropertyOrder("ei", "budgetBreakdown", "funder", "payer", "buyer")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponseDto(

        @JsonProperty("ei")
        val ei: Set<String>?,

        @Valid
        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<CheckBudgetBreakdownDto>?,

        @Valid
        @JsonProperty("funder")
        val funder: HashSet<FsOrganizationReferenceDto>?,

        @Valid
        @JsonProperty("payer")
        val payer: HashSet<FsOrganizationReferenceDto>?,

        @Valid
        @JsonProperty("buyer")
        val buyer: HashSet<EiOrganizationReferenceDto>?
)
