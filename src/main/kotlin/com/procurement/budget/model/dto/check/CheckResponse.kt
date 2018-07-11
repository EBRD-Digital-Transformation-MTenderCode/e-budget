package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.model.dto.ei.EiOrganizationReference
import com.procurement.budget.model.dto.fs.FsOrganizationReference
import java.util.*

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckResponse(

        val ei: Set<String>?,

        val budgetBreakdown: List<CheckBudgetBreakdown>,

        val funder: HashSet<FsOrganizationReference>?,

        val payer: HashSet<FsOrganizationReference>?,

        val buyer: HashSet<EiOrganizationReference>?
)
