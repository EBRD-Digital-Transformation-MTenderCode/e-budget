package com.procurement.budget.model.dto.ei

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.MoneyDeserializer
import com.procurement.budget.model.dto.ocds.*
import com.procurement.budget.model.dto.ocds.Currency
import java.math.BigDecimal
import java.util.*
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Ei @JsonCreator constructor(

        var token: String? = null,

        var ocid: String,

        var tender: TenderEi,

        var planning: PlanningEi,

        var buyer: OrganizationReferenceEi
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderEi @JsonCreator constructor(

        var id: String,

        var title: String,

        var description: String?,

        var status: TenderStatus,

        var statusDetails: TenderStatusDetails,

        val classification: Classification,

        val mainProcurementCategory: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlanningEi @JsonCreator constructor(

        val budget: BudgetEi,

        var rationale: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetEi @JsonCreator constructor(

        var id: String,

        var period: Period,

        @field:Valid
        var amount: ValueEi?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValueEi @JsonCreator constructor(

        @field:JsonDeserialize(using = MoneyDeserializer::class)
        var amount: BigDecimal,

        var currency: Currency
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReferenceEi @JsonCreator constructor(

        var id: String?,

        val name: String,

        val identifier: Identifier,

        val address: Address,

        val additionalIdentifiers: HashSet<Identifier>?,

        val contactPoint: ContactPoint,

        val details: Details,

        val buyerProfile: String?
)
