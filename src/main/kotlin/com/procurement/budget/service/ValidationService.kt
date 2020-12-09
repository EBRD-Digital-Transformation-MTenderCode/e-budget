package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType.ADDITIONAL_IDENTIFIERS_IN_BUYER_IS_EMPTY_OR_MISSING
import com.procurement.budget.exception.ErrorType.BANK_ACCOUNTS_IN_DETAILS_IN_BUYER_IS_EMPTY_OR_MISSING
import com.procurement.budget.exception.ErrorType.BF
import com.procurement.budget.exception.ErrorType.BUSINESS_FUNCTIONS_IN_PERSON_IN_BUYER_IS_EMPTY_OR_MISSING
import com.procurement.budget.exception.ErrorType.DOCUMENTS
import com.procurement.budget.exception.ErrorType.DOCUMENTS_BUSINESS_FUNCTION_IN_PERSON_IN_BUYER_IS_EMPTY_OR_MISSING
import com.procurement.budget.exception.ErrorType.EI_NOT_FOUND
import com.procurement.budget.exception.ErrorType.FS_NOT_FOUND
import com.procurement.budget.exception.ErrorType.INVALID_AMOUNT
import com.procurement.budget.exception.ErrorType.INVALID_BA
import com.procurement.budget.exception.ErrorType.INVALID_BA_ID
import com.procurement.budget.exception.ErrorType.INVALID_BA_PERIOD
import com.procurement.budget.exception.ErrorType.INVALID_BUDGET_BREAKDOWN_ID
import com.procurement.budget.exception.ErrorType.INVALID_BUSINESS_FUNCTION_TYPE
import com.procurement.budget.exception.ErrorType.INVALID_BUYER_ID
import com.procurement.budget.exception.ErrorType.INVALID_CPV
import com.procurement.budget.exception.ErrorType.INVALID_CURRENCY
import com.procurement.budget.exception.ErrorType.INVALID_STATUS
import com.procurement.budget.exception.ErrorType.PERSONES
import com.procurement.budget.exception.ErrorType.PERSONES_IN_BUYER_IS_EMPTY_OR_MISSING
import com.procurement.budget.model.dto.bpe.CommandMessage
import com.procurement.budget.model.dto.bpe.ResponseDto
import com.procurement.budget.model.dto.check.BudgetBreakdownCheckRq
import com.procurement.budget.model.dto.check.BudgetBreakdownCheckRs
import com.procurement.budget.model.dto.check.BudgetCheckRs
import com.procurement.budget.model.dto.check.BudgetSource
import com.procurement.budget.model.dto.check.CheckBsRq
import com.procurement.budget.model.dto.check.CheckBsRs
import com.procurement.budget.model.dto.check.CheckRq
import com.procurement.budget.model.dto.check.CheckRs
import com.procurement.budget.model.dto.check.CheckSourceParty
import com.procurement.budget.model.dto.check.CheckValue
import com.procurement.budget.model.dto.check.OrganizationReferenceBuyer
import com.procurement.budget.model.dto.check.PlanningCheckRs
import com.procurement.budget.model.dto.check.TenderCheckRs
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.Fs
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.BusinessFunction
import com.procurement.budget.model.dto.ocds.DocumentBF
import com.procurement.budget.model.dto.ocds.Person
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.utils.toObject
import com.procurement.budget.utils.toSetBy
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class ValidationService(private val fsDao: FsDao,
                        private val eiDao: EiDao) {

    fun checkFs(cm: CommandMessage): ResponseDto {
        val dto = toObject(CheckRq::class.java, cm.data)
        val breakdownsRq = dto.planning.budget.budgetBreakdown
        validateBudgetBreakdown(breakdownsRq)
        val cpIds = breakdownsRq.toSetBy { getCpIdFromOcId(it.id) }
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        entities.asSequence()
                .map { toObject(Fs::class.java, it.jsonData) }
                .forEach { fsMap[it.ocid] = it }

        val funders = mutableListOf<OrganizationReferenceFs>()
        val payers = mutableListOf<OrganizationReferenceFs>()
        val buyers = mutableListOf<OrganizationReferenceEi>()
        val breakdownsRs: ArrayList<BudgetBreakdownCheckRs> = arrayListOf()
        var isEuropeanUnionFunded = false
        var totalAmount: BigDecimal = BigDecimal.ZERO
        val totalCurrency = dto.planning.budget.budgetBreakdown[0].amount.currency
        var mainProcurementCategory = ""
        for (cpId in cpIds) {
            val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
            val ei = toObject(Ei::class.java, eiEntity.jsonData)
            mainProcurementCategory = ei.tender.mainProcurementCategory
            checkCPV(ei, dto)
            buyers.add(ei.buyer)
            breakdownsRq.filter { cpId == getCpIdFromOcId(it.id) }.forEach { br ->
                val fs = fsMap[br.id] ?: throw ErrorException(FS_NOT_FOUND)
                if (fs.planning.budget.isEuropeanUnionFunded) isEuropeanUnionFunded = true
                totalAmount += br.amount.amount
                checkFsStatus(fs)
                checkFsAmount(fs, br)
                checkFsCurrency(fs, br)
                fs.funder?.let { funders.add(it) }
                fs.payer.let { payers.add(it) }
                breakdownsRs.add(getBudgetBreakdown(br, fs))
            }
        }

        return ResponseDto(data =
        CheckRs(
                ei = cpIds.toList(),
                planning = PlanningCheckRs(
                        budget = BudgetCheckRs(
                                description = dto.planning.budget.description,
                                amount = CheckValue(totalAmount, totalCurrency),
                                isEuropeanUnionFunded = isEuropeanUnionFunded,
                                budgetBreakdown = breakdownsRs
                        ),
                        rationale = dto.planning.rationale
                ),
                tender = TenderCheckRs(
                        mainProcurementCategory = mainProcurementCategory,
                        classification = dto.tender.classification),
                funder = funders,
                payer = payers,
                buyer = buyers)
        )
    }

    fun checkBudgetSources(cm: CommandMessage): ResponseDto {
        val dto = toObject(CheckBsRq::class.java, cm.data)

        checkBuyerPersones(dto.buyer)
        checkBuyerDetailsBankAccounts(dto.buyer)
        checkBuyerAdditionalIdentifiers(dto.buyer)
        checkBuyerPersonesBusinessFunctionsType(dto.buyer)

        val budgetSourcesRq = dto.planning.budget.budgetSource
        val actualBudgetSourcesRq = dto.actualBudgetSource
        val budgetAllocationRq = dto.planning.budget.budgetAllocation
        val newEiIds = budgetSourcesRq.toSetBy { getCpIdFromOcId(it.budgetBreakdownID) }
        val entities = fsDao.getAllByCpIds(newEiIds)
        if (entities.isEmpty()) throw ErrorException(FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        val funders = mutableListOf<OrganizationReferenceFs>()
        val payers = mutableListOf<OrganizationReferenceFs>()
        var buyer: OrganizationReferenceEi? = null
        val cpvCodesFromEi = mutableListOf<String>()
        val treasuryBudgetSources = mutableListOf<BudgetSource>()
        entities.asSequence().map { toObject(Fs::class.java, it.jsonData) }.forEach { fsMap[it.ocid] = it }
        for (cpId in newEiIds) {
            val bsIds = budgetSourcesRq.toSetBy { it.budgetBreakdownID }
            val baIds = budgetAllocationRq.toSetBy { it.budgetBreakdownID }
            if (bsIds.size != baIds.size) throw ErrorException(INVALID_BA)
            if (!bsIds.containsAll(baIds)) throw ErrorException(INVALID_BA_ID)
            if (budgetSourcesRq.toSetBy { it.currency }.size > 1) throw ErrorException(INVALID_CURRENCY)
            budgetSourcesRq.asSequence().filter { cpId == getCpIdFromOcId(it.budgetBreakdownID) }.forEach { bsRq ->
                val fs = fsMap[bsRq.budgetBreakdownID] ?: throw ErrorException(FS_NOT_FOUND)
//                if (fs.tender.status != TenderStatus.ACTIVE) throw ErrorException(INVALID_STATUS)
//                if (fs.tender.statusDetails != TenderStatusDetails.EMPTY) throw ErrorException(INVALID_STATUS)
                val fsValue = fs.planning.budget.amount
                if (fsValue.currency != bsRq.currency) throw ErrorException(INVALID_CURRENCY)
                if (fsValue.amount < bsRq.amount) throw ErrorException(INVALID_AMOUNT)
                if (fs.funder != null) {
                    funders.add(fs.funder)
                } else {
                    bsRq.budgetIBAN = fs.planning.budget.id
                    treasuryBudgetSources.add(bsRq)
                }
                payers.add(fs.payer)
            }
            budgetAllocationRq.asSequence().filter { cpId == getCpIdFromOcId(it.budgetBreakdownID) }.forEach { ba ->
                val fs = fsMap[ba.budgetBreakdownID] ?: throw ErrorException(FS_NOT_FOUND)
                if (ba.period.startDate > ba.period.endDate) throw ErrorException(INVALID_BA_PERIOD)
                val fsPeriod = fs.planning.budget.period
                if (ba.period.startDate < fsPeriod.startDate) throw ErrorException(INVALID_BA_PERIOD)
                if (ba.period.endDate > fsPeriod.endDate) throw ErrorException(INVALID_BA_PERIOD)
            }
            val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(EI_NOT_FOUND)
            val ei = toObject(Ei::class.java, eiEntity.jsonData)
            buyer = updateBuyer(ei.buyer, dto.buyer)// BR-9.2.21
            cpvCodesFromEi.add(ei.tender.classification.id.substring(0, 3).toUpperCase())
        }
        validateCpv(cpvCodesFromEi, dto.itemsCPVs)

        val addedEI: Set<String>
        val excludedEI: Set<String>
        val addedFS: Set<String>
        val excludedFS: Set<String>
        val newFsIds = budgetSourcesRq.toSetBy { it.budgetBreakdownID }
        if (actualBudgetSourcesRq != null && actualBudgetSourcesRq.isNotEmpty()) {
            val actualEiIds = actualBudgetSourcesRq.toSetBy { getCpIdFromOcId(it.budgetBreakdownID) }
            val (toAddEi, toExcludeEi) = diff(left = actualEiIds, right = newEiIds)
            addedEI = toAddEi
            excludedEI = toExcludeEi

            val actualFsIds = actualBudgetSourcesRq.toSetBy { it.budgetBreakdownID }
            val (toAddFs, toExcludeFs) = diff(left = actualFsIds, right = newFsIds)
            addedFS = toAddFs
            excludedFS = toExcludeFs
        } else {
            addedEI = newEiIds
            excludedEI = emptySet()
            addedFS = newFsIds
            excludedFS = emptySet()
        }
        return ResponseDto(
            data = CheckBsRs(
                treasuryBudgetSources = when {
                    treasuryBudgetSources.isNotEmpty() -> treasuryBudgetSources
                    else -> null
                },
                buyer = buyer,
                funders = funders,
                payers = payers,
                addedEI = addedEI.toList(),
                excludedEI = excludedEI.toList(),
                addedFS = addedFS.toList(),
                excludedFS = excludedFS.toList()
            )
        )
    }

    private fun <T> diff(left: Set<T>, right: Set<T>): Pair<Set<T>, Set<T>> {
        val allElements = left + right
        val addToLeft = mutableSetOf<T>()
        val addToRight = mutableSetOf<T>()

        allElements.forEach { element ->
            if (left.contains(element) && !right.contains(element))
                addToRight.add(element)

            if (!left.contains(element) && right.contains(element))
                addToLeft.add(element)
        }

        return addToLeft to addToRight
    }

    /**
     * VR-10.6.11
     */
    private fun checkBuyerPersones(buyer: OrganizationReferenceBuyer) {
        if (buyer.persones.isEmpty())
            throw ErrorException(error = PERSONES_IN_BUYER_IS_EMPTY_OR_MISSING)

        buyer.persones.forEach {
            checkBuyerPersonBusinessFunctions(it)
        }
    }

    /**
     * VR-10.6.12
     */
    private fun checkBuyerPersonBusinessFunctions(person: Person) {
        if (person.businessFunctions.isEmpty())
            throw ErrorException(error = BUSINESS_FUNCTIONS_IN_PERSON_IN_BUYER_IS_EMPTY_OR_MISSING)

        person.businessFunctions.forEach {
            checkBuyerPersonBusinessFunctionDocuments(it)
        }
    }

    /**
     * VR-10.6.13
     */
    private fun checkBuyerPersonBusinessFunctionDocuments(businessFunctions: BusinessFunction) {
        if (businessFunctions.documents.isEmpty())
            throw ErrorException(error = DOCUMENTS_BUSINESS_FUNCTION_IN_PERSON_IN_BUYER_IS_EMPTY_OR_MISSING)
    }

    /**
     * VR-10.6.14
     */
    private fun checkBuyerDetailsBankAccounts(buyer: OrganizationReferenceBuyer) {
        if (buyer.details == null || buyer.details.bankAccounts == null || buyer.details.bankAccounts.isEmpty())
            throw ErrorException(error = BANK_ACCOUNTS_IN_DETAILS_IN_BUYER_IS_EMPTY_OR_MISSING)
    }

    /**
     * VR-10.6.15
     */
    private fun checkBuyerAdditionalIdentifiers(buyer: OrganizationReferenceBuyer) {
        if (buyer.additionalIdentifiers == null || buyer.additionalIdentifiers.isEmpty())
            throw ErrorException(error = ADDITIONAL_IDENTIFIERS_IN_BUYER_IS_EMPTY_OR_MISSING)
    }

    /**
     * VR-10.6.16
     */
    private fun checkBuyerPersonesBusinessFunctionsType(buyer: OrganizationReferenceBuyer) {
        buyer.persones.forEach {person ->
            person.businessFunctions.forEach {businessFunction ->
                if(businessFunction.type ==  "authority")
                    return
            }
        }
        throw ErrorException(error = INVALID_BUSINESS_FUNCTION_TYPE)
    }

    private fun validateCpv(cpvCodesFromEi: List<String>, itemsCPVs: List<String>) {
        if (cpvCodesFromEi.size > 1) throw ErrorException(INVALID_CPV)
        val itemsCPVSet = itemsCPVs.map { it.substring(0, 3).toUpperCase() }
        if (itemsCPVSet.size > 1) throw ErrorException(INVALID_CPV)
        if (!cpvCodesFromEi.containsAll(itemsCPVSet)) throw ErrorException(INVALID_CPV)
    }

    private fun updateBuyer(buyerDb: OrganizationReferenceEi, buyerDto: OrganizationReferenceBuyer): OrganizationReferenceEi {
        //validation
        if (buyerDb.id != buyerDto.id) throw ErrorException(INVALID_BUYER_ID)
        //update
        buyerDb.persones = updatePersones(buyerDb.persones, buyerDto.persones)//BR-9.2.3
        buyerDb.additionalIdentifiers = buyerDto.additionalIdentifiers
        buyerDb.details = buyerDto.details
        return buyerDb
    }

    private fun updatePersones(personesDb: List<Person>?, personesDto: List<Person>): List<Person> {
        if (personesDb == null || personesDb.isEmpty()) return personesDto
        val personesDbIds = personesDb.toSetBy { it.identifier.id }
        val personesDtoIds = personesDto.toSetBy { it.identifier.id }
        if (personesDtoIds.size != personesDto.size) throw ErrorException(PERSONES)
        //update
        personesDb.forEach { personDb -> personDb.update(personesDto.first { it.identifier.id == personDb.identifier.id }) }
        val newPersonesId = personesDtoIds - personesDbIds
        val newPersones = personesDto.filter { it.identifier.id in newPersonesId }
        return (personesDb + newPersones)
    }

    private fun Person.update(personDto: Person) {
        this.title = personDto.title
        this.name = personDto.name
        this.businessFunctions = updateBusinessFunctions(this.businessFunctions, personDto.businessFunctions)
    }

    private fun updateBusinessFunctions(bfDb: List<BusinessFunction>, bfDto: List<BusinessFunction>): List<BusinessFunction> {
        if (bfDb.isEmpty()) return bfDto
        val bfDbIds = bfDb.toSetBy { it.id }
        val bfDtoIds = bfDto.toSetBy { it.id }
        if (bfDtoIds.size != bfDto.size) throw ErrorException(BF)
        //update
        bfDb.forEach { bf -> bf.update(bfDto.first { it.id == bf.id }) }
        val newBfId = bfDtoIds - bfDbIds
        val newBf = bfDto.filter { it.id in newBfId }
        return bfDb + newBf
    }

    private fun BusinessFunction.update(bfDto: BusinessFunction) {
        this.type = bfDto.type
        this.jobTitle = bfDto.jobTitle
        this.period = bfDto.period
        this.documents = updateDocuments(this.documents, bfDto.documents)
    }

    private fun updateDocuments(documentsDb: List<DocumentBF>, documentsDto: List<DocumentBF>): List<DocumentBF> {
        //validation
        val documentsDbIds = documentsDb.toSetBy { it.id }
        val documentDtoIds = documentsDto.toSetBy { it.id }
        if (documentDtoIds.size != documentsDto.size) throw ErrorException(DOCUMENTS)
        //update
        documentsDb.forEach { docDb -> docDb.update(documentsDto.first { it.id == docDb.id }) }
        val newDocumentsId = documentDtoIds - documentsDbIds
        val newDocuments = documentsDto.asSequence().filter { it.id in newDocumentsId }.toList()
        return (documentsDb + newDocuments)
    }

    private fun DocumentBF.update(documentDto: DocumentBF) {
        this.title = documentDto.title
        this.description = documentDto.description
    }

    private fun validateBudgetBreakdown(budgetBreakdown: List<BudgetBreakdownCheckRq>) {
        if (budgetBreakdown.toSetBy { it.amount.currency }.size > 1) throw ErrorException(INVALID_CURRENCY)
        if (budgetBreakdown.toSetBy { it.id }.size < budgetBreakdown.size) throw ErrorException(INVALID_BUDGET_BREAKDOWN_ID)
    }

    private fun checkCPV(ei: Ei, dto: CheckRq) {
        if (dto.tender.classification != null) {
            val dtoCPV = dto.tender.classification!!.id
            val eiCPV = ei.tender.classification.id
            if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase()) throw ErrorException(INVALID_CPV)
        } else {
            dto.tender.classification = ei.tender.classification
        }
    }

    private fun getBudgetBreakdown(breakdownRq: BudgetBreakdownCheckRq, fs: Fs): BudgetBreakdownCheckRs {
        return BudgetBreakdownCheckRs(
                id = fs.ocid,
                description = fs.planning.budget.description,
                period = fs.planning.budget.period,
                amount = breakdownRq.amount,
                europeanUnionFunding = fs.planning.budget.europeanUnionFunding,
                sourceParty = CheckSourceParty(
                        id = fs.planning.budget.sourceEntity.id,
                        name = fs.planning.budget.sourceEntity.name)
        )
    }

    private fun checkFsCurrency(fs: Fs, br: BudgetBreakdownCheckRq) {
        val fsCurrency = fs.planning.budget.amount.currency
        val brCurrency = br.amount.currency
        if (fsCurrency != brCurrency) throw ErrorException(INVALID_CURRENCY)
    }

    private fun checkFsAmount(fs: Fs, br: BudgetBreakdownCheckRq) {
        val brAmount = br.amount.amount
        val fsAmount = fs.planning.budget.amount.amount
        if (brAmount > fsAmount) throw ErrorException(INVALID_AMOUNT)
    }

    private fun checkFsStatus(fs: Fs) {
        val fsStatus = fs.tender.status
        val fsStatusDetails = fs.tender.statusDetails
        if (fsStatusDetails != TenderStatusDetails.EMPTY) throw ErrorException(INVALID_STATUS)
        if (!((fsStatus == TenderStatus.ACTIVE || fsStatus == TenderStatus.PLANNING || fsStatus == TenderStatus.PLANNED)))
            throw ErrorException(INVALID_STATUS)
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf("-FS-")
        if (pos < 0) throw ErrorException(INVALID_BUDGET_BREAKDOWN_ID, ocId)
        return ocId.substring(0, pos)
    }
}



