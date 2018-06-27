package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class Details @JsonCreator constructor(

        @field:Valid
        @field:NotNull
        val typeOfBuyer: TypeOfBuyer,

        @field:Valid
        @field:NotNull
        val mainGeneralActivity: MainGeneralActivity,

        @field:Valid
        @field:NotNull
        val mainSectoralActivity: MainSectoralActivity,

        @field:NotNull
        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        @field:Valid
        @field:NotNull
        val scale: Scale
)
