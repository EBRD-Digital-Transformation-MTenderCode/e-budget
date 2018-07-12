package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import java.util.*

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponse(

        val ei: Set<String>?,

        val budgetBreakdown: List<CheckBudgetBreakdown>,

        val funder: HashSet<OrganizationReferenceFs>?,

        val payer: HashSet<OrganizationReferenceFs>?,

        val buyer: HashSet<OrganizationReferenceEi>?
)
