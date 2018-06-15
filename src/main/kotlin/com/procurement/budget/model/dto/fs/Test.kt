package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

class Test @JsonCreator constructor(
        
            @param:JsonProperty("isEuropeanUnionFunded") 
            @field:NotNull
            @field:JsonProperty("isEuropeanUnionFunded")
            private val isEuropeanUnionFunded: Boolean?)
