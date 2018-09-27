package com.procurement.budget.model.dto.fs.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.fs.ValueFs
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period

data class FsCreate @JsonCreator constructor(

        var tender: TenderFsCreate,

        var planning: PlanningFsCreate,

        val buyer: OrganizationReferenceFs?
)


data class TenderFsCreate @JsonCreator constructor(

        val procuringEntity: OrganizationReferenceFs
)

data class PlanningFsCreate @JsonCreator constructor(

        val budget: BudgetFsCreate,

        var rationale: String?
)

data class BudgetFsCreate @JsonCreator constructor(

        var id: String?,

        var description: String?,

        val period: Period,

        val amount: ValueFs,

        @field:JsonDeserialize(using = BooleansDeserializer::class)
        val isEuropeanUnionFunded: Boolean,

        var europeanUnionFunding: EuropeanUnionFunding?,

        val project: String?,

        val projectID: String?,

        val uri: String?
)

