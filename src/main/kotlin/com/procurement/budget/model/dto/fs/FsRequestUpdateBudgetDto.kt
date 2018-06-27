package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class FsRequestUpdateBudgetDto  @JsonCreator constructor(

        val id: String?,

        val description: String?,

        @field:Valid
        @field:NotNull
        val period: Period,

        @field:Valid
        @field:NotNull
        val amount: FsValue,

        @field:Valid
        val europeanUnionFunding: EuropeanUnionFunding?,

        @field:NotNull
        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @field:NotNull
        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("verified")
        var verified: Boolean?,

        @field:NotNull
        @field:Valid
        var sourceEntity: FsRequestUpdateBudgetOrganizationReferenceDto,

        val verificationDetails: String?
)
