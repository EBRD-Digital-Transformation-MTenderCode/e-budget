package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.Details
import com.procurement.budget.model.dto.ocds.Identifier
import com.procurement.budget.model.dto.ocds.Period
import com.procurement.budget.model.dto.ocds.Person
import java.math.BigDecimal

data class CheckBsRq @JsonCreator constructor(

        val planning: Planning,

        val buyer: OrganizationReferenceBuyer,

        val actualBudgetSource: Set<BudgetSource>?,

        val itemsCPVs: HashSet<String>
)

data class CheckBsRs @JsonCreator constructor(

        val treasuryBudgetSources: Set<BudgetSource>?,

        val buyer: OrganizationReferenceEi?,

        val funders: HashSet<OrganizationReferenceFs>?,

        val payers: HashSet<OrganizationReferenceFs>?,

        val addedEI: Set<String>?,

        val excludedEI: Set<String>?,

        val addedFS: Set<String>?,

        val excludedFS: Set<String>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Planning @JsonCreator constructor(

        val budget: Budget
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Budget @JsonCreator constructor(

        val budgetAllocation: Set<BudgetAllocation>,

        val budgetSource: Set<BudgetSource>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetAllocation @JsonCreator constructor(

        val budgetBreakdownID: String,

        val period: Period,

        val relatedItem: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetSource @JsonCreator constructor(

        val budgetBreakdownID: String,

        var budgetIBAN: String?,

        @JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal,

        val currency: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceBuyer @JsonCreator constructor(

        var id: String?,

        val name: String?,

        val additionalIdentifiers: HashSet<Identifier>?,

        val persones: HashSet<Person>,

        val details: Details?
)