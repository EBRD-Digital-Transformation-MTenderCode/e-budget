package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponseDto(

        val ei: Set<String>?,

        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        val funder: HashSet<FsOrganizationReferenceDto>?,

        val payer: HashSet<FsOrganizationReferenceDto>?,

        val buyer: HashSet<EiOrganizationReferenceDto>?
)
