package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("typeOfBuyer", "mainGeneralActivity", "mainSectoralActivity", "isACentralPurchasingBody", "NUTSCode", "scale")
data class Details(

        @Valid
        @JsonProperty("typeOfBuyer")
        val typeOfBuyer: TypeOfBuyer,

        @Valid
        @JsonProperty("mainGeneralActivity")
        val mainGeneralActivity: MainGeneralActivity,

        @Valid
        @JsonProperty("mainSectoralActivity")
        val mainSectoralActivity: MainSectoralActivity,

        @JsonProperty("isACentralPurchasingBody")
        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        @JsonProperty("NUTSCode")
        val nutsCode: String?,

        @Valid
        @JsonProperty("scale")
        val scale: Scale
)
