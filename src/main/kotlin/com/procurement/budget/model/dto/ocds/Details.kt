package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.budget.model.dto.databinding.BooleansDeserializer
import javax.validation.constraints.NotNull

data class Details @JsonCreator constructor(

        @field:NotNull
        val typeOfBuyer: TypeOfBuyer,

        @field:NotNull
        val mainGeneralActivity: MainGeneralActivity,

        @field:NotNull
        val mainSectoralActivity: MainSectoralActivity,

        @field:NotNull
        @field:JsonDeserialize(using = BooleansDeserializer::class)
        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        @field:NotNull
        val scale: Scale
)
