package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ei.EiOrganizationReference
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import java.util.*

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponseDto(

        val ei: Set<String>?,

        val budgetBreakdown: List<CheckBudgetBreakdownDto>,

        val funder: HashSet<FsOrganizationReferenceDto>?,

        val payer: HashSet<FsOrganizationReferenceDto>?,

        val buyer: HashSet<EiOrganizationReference>?
)
