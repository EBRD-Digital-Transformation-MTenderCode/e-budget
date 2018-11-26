package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType.*
import com.procurement.budget.model.dto.bpe.CommandMessage
import com.procurement.budget.model.dto.bpe.ResponseDto
import com.procurement.budget.model.dto.check.*
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.Fs
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.*
import com.procurement.budget.utils.toObject
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
        val cpIds = breakdownsRq.asSequence().map { getCpIdFromOcId(it.id) }.toSet()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        entities.asSequence()
                .map { toObject(Fs::class.java, it.jsonData) }
                .forEach { fsMap[it.ocid] = it }

        val funders = HashSet<OrganizationReferenceFs>()
        val payers = HashSet<OrganizationReferenceFs>()
        val buyers = HashSet<OrganizationReferenceEi>()
        val breakdownsRs: ArrayList<BudgetBreakdownCheckRs> = arrayListOf()
        var isEuropeanUnionFunded = false
        var totalAmount: BigDecimal = BigDecimal.ZERO
        var totalCurrency = dto.planning.budget.budgetBreakdown[0].amount.currency
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
                ei = cpIds,
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
        val budgetSourcesRq = dto.planning.budget.budgetSource
        val actualBudgetSourcesRq = dto.actualBudgetSource
        val budgetAllocationRq = dto.planning.budget.budgetAllocation
        val cpIds = budgetSourcesRq.asSequence().map { getCpIdFromOcId(it.budgetBreakdownID) }.toSet()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        val funders = HashSet<OrganizationReferenceFs>()
        val payers = HashSet<OrganizationReferenceFs>()
        var buyer: OrganizationReferenceEi? = null
        val cpvCodesFromEi = HashSet<String>()
        entities.asSequence().map { toObject(Fs::class.java, it.jsonData) }.forEach { fsMap[it.ocid] = it }
        for (cpId in cpIds) {
            val bsIds = budgetSourcesRq.asSequence().map { it.budgetBreakdownID }.toSet()
            val baIds = budgetAllocationRq.asSequence().map { it.budgetBreakdownID }.toSet()
            if (bsIds.size != baIds.size) throw ErrorException(INVALID_BA)
            if (!bsIds.containsAll(baIds)) throw ErrorException(INVALID_BA_ID)
            if (budgetSourcesRq.asSequence().map { it.currency }.toSet().size > 1) throw ErrorException(INVALID_CURRENCY)
            budgetSourcesRq.asSequence().filter { cpId == getCpIdFromOcId(it.budgetBreakdownID) }.forEach { bs ->
                val fs = fsMap[bs.budgetBreakdownID] ?: throw ErrorException(FS_NOT_FOUND)
//                if (fs.tender.status != TenderStatus.ACTIVE) throw ErrorException(INVALID_STATUS)
//                if (fs.tender.statusDetails != TenderStatusDetails.EMPTY) throw ErrorException(INVALID_STATUS)
                val fsValue = fs.planning.budget.amount
                if (fsValue.currency != bs.currency) throw ErrorException(INVALID_CURRENCY)
                if (fsValue.amount < bs.amount) throw ErrorException(INVALID_AMOUNT)
                fs.funder?.let { funders.add(it) }
                fs.payer.let { payers.add(it) }
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
            updateBuyer(ei.buyer, dto.buyer)// BR-9.2.21
            buyer = ei.buyer
            cpvCodesFromEi.add(ei.tender.classification.id.substring(0, 3).toUpperCase())
        }
        if (cpvCodesFromEi.size > 1) throw ErrorException(INVALID_CPV)
        if (cpvCodesFromEi.containsAll(dto.itemsCPVs)) throw ErrorException(INVALID_CPV)
        var addedEI: Set<String>? = null
        var excludedEI: Set<String>? = null
        var addedFS: Set<String>? = null
        var excludedFS: Set<String>? = null
        val fsIds = budgetSourcesRq.asSequence().map { it.budgetBreakdownID }.toSet()
        if (actualBudgetSourcesRq != null) {
            val actualCpIds = actualBudgetSourcesRq.asSequence().map { getCpIdFromOcId(it.budgetBreakdownID) }.toSet()
            if (cpIds.size == actualCpIds.size && cpIds.containsAll(actualCpIds)) {
                excludedEI = actualCpIds - cpIds
                addedEI = cpIds - actualCpIds
            }
            val actualFsIds = actualBudgetSourcesRq.asSequence().map { it.budgetBreakdownID }.toSet()
            if (fsIds.size == actualFsIds.size && fsIds.containsAll(actualFsIds)) {
                excludedFS = actualFsIds - fsIds
                addedFS = fsIds - actualFsIds
            }
        } else {
            addedEI = cpIds
            addedFS = fsIds
        }

        return ResponseDto(data = CheckBsRs(
                treasuryBudgetSources = budgetSourcesRq,
                buyer = buyer,
                funders = funders,
                payers = payers,
                addedEI = addedEI,
                excludedEI = excludedEI,
                addedFS = addedFS,
                excludedFS = excludedFS)
        )
    }

    private fun updateBuyer(buyerDb: OrganizationReferenceEi, buyerDto: OrganizationReferenceBuyer) {
        //validation
        if (buyerDb.id != buyerDto.id) throw ErrorException(INVALID_BUYER_ID)
        //update
        buyerDb.persones = updatePersones(buyerDb.persones, buyerDto.persones)//BR-9.2.3
        buyerDb.additionalIdentifiers = buyerDto.additionalIdentifiers
        buyerDb.details = buyerDto.details
    }

    private fun updatePersones(personesDb: HashSet<Person>?, personesDto: HashSet<Person>): HashSet<Person> {
        if (personesDb == null || personesDb.isEmpty()) return personesDto
        val personesDbIds = personesDb.asSequence().map { it.identifier.id }.toSet()
        val personesDtoIds = personesDto.asSequence().map { it.identifier.id }.toSet()
        if (personesDtoIds.size != personesDto.size) throw ErrorException(PERSONES)
        //update
        personesDb.forEach { personDb -> personDb.update(personesDto.first { it.identifier.id == personDb.identifier.id }) }
        val newPersonesId = personesDtoIds - personesDbIds
        val newPersones = personesDto.asSequence().filter { it.identifier.id in newPersonesId }.toHashSet()
        return (personesDb + newPersones).toHashSet()
    }

    private fun Person.update(personDto: Person) {
        this.title = personDto.title
        this.name = personDto.name
        this.businessFunctions = updateBusinessFunctions(this.businessFunctions, personDto.businessFunctions)
    }

    private fun updateBusinessFunctions(bfDb: List<BusinessFunction>, bfDto: List<BusinessFunction>): List<BusinessFunction> {
        if (bfDb.isEmpty()) return bfDto
        val bfDbIds = bfDb.asSequence().map { it.id }.toSet()
        val bfDtoIds = bfDto.asSequence().map { it.id }.toSet()
        if (bfDtoIds.size != bfDto.size) throw ErrorException(BF)
        //update
        bfDb.forEach { bf -> bf.update(bfDto.first { it.id == bf.id }) }
        val newBfId = bfDtoIds - bfDbIds
        val newBf = bfDto.asSequence().filter { it.id in newBfId }.toHashSet()
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
        val documentsDbIds = documentsDb.asSequence().map { it.id }.toSet()
        val documentDtoIds = documentsDto.asSequence().map { it.id }.toSet()
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
        if (budgetBreakdown.asSequence().map { it.amount.currency }.toSet().size > 1) throw ErrorException(INVALID_CURRENCY)
        if (budgetBreakdown.asSequence().map { it.id }.toSet().size < budgetBreakdown.size) throw ErrorException(INVALID_BUDGET_BREAKDOWN_ID)
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



