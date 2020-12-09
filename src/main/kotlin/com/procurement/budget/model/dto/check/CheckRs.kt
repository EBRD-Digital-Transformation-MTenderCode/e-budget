package com.procurement.budget.model.dto.check

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.Classification
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckRs(

        val ei: List<String>?,

        val planning: PlanningCheckRs,

        var tender: TenderCheckRs,

        val funder: List<OrganizationReferenceFs>?,

        val payer: List<OrganizationReferenceFs>?,

        val buyer: List<OrganizationReferenceEi>?
)

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class PlanningCheckRs @JsonCreator constructor(

        val budget: BudgetCheckRs,

        var rationale: String?
)

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class BudgetCheckRs @JsonCreator constructor(

        var description: String?,

        val amount: CheckValue,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean,

        val budgetBreakdown: List<BudgetBreakdownCheckRs>
)

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class BudgetBreakdownCheckRs @JsonCreator constructor(

        val id: String,

        val description: String?,

        val amount: CheckValue,

        var period: Period,

        var sourceParty: CheckSourceParty,

        var europeanUnionFunding: EuropeanUnionFunding?
)

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class CheckSourceParty @JsonCreator constructor(

        val id: String,

        val name: String
)

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class TenderCheckRs @JsonCreator constructor(

        val mainProcurementCategory: String,

        var classification: Classification?
)