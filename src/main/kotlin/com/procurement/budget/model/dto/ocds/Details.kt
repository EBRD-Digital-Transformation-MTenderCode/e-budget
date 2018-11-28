package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: TypeOfBuyer?,

        val mainGeneralActivity: MainGeneralActivity?,

        val mainSectoralActivity: MainSectoralActivity?,

        val permits: List<Permits>,

        val gpaProfile: GpaProfile?,

        val bankAccounts: Set<BankAccount>?,

        val legalForm: LegalForm?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Permits @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val url: String?,

        val permitDetails: PermitDetails
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PermitDetails @JsonCreator constructor(

        val issuedBy: Issue,

        val issuedThought: Issue,

        val validityPeriod: ValidityPeriod
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValidityPeriod @JsonCreator constructor(

        val startDate: LocalDateTime,

        val endDate: LocalDateTime
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Issue @JsonCreator constructor(

        val id: String,

        val name: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaProfile @JsonCreator constructor(

        val gpaAnnex: GpaAnnex?,

        val gpaOrganizationType: GpaOrganizationType?,

        val gpaThresholds: Set<GpaThreshold>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaAnnex @JsonCreator constructor(

        val id: String?,

        val legalName: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaOrganizationType @JsonCreator constructor(

        val id: String?,

        val legalName: String?,

        val uri: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaThreshold @JsonCreator constructor(

        val mainProcurementCategory: String?,

        val value: Value?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BankAccount @JsonCreator constructor(

        val description: String?,

        val bankName: String?,

        val address: Address?,

        val identifier: AccountIdentifier,

        val accountIdentification: AccountIdentifier,

        val additionalAccountIdentifiers: Set<AccountIdentifier>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountIdentifier @JsonCreator constructor(

        val id: String?,

        val scheme: String?
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class LegalForm @JsonCreator constructor(

        val id: String?,

        val scheme: String?,

        val description: String?,

        val uri: String?
)