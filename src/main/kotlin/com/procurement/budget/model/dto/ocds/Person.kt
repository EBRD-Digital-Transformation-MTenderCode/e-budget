package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Person @JsonCreator constructor(

        var title: String,

        var name: String,

        val identifier: PersonIdentifier,

        var businessFunctions: List<BusinessFunction>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BusinessFunction @JsonCreator constructor(

        val id: String,

        var type: String,

        var jobTitle: String,

        var period: PeriodBF,

        var documents: List<DocumentBF>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DocumentBF @JsonCreator constructor(

        val id: String,

        val documentType: String,

        var title: String?,

        var description: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PersonIdentifier @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val legalName: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PeriodBF @JsonCreator constructor(

        val startDate: LocalDateTime
)