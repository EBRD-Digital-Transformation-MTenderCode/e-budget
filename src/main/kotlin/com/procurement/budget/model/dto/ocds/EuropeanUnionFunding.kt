package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EuropeanUnionFunding @JsonCreator constructor(

        val projectName: String,

        val projectIdentifier: String,

        val uri: String?
)
