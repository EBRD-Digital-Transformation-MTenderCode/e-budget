package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.*
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.Fs
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

interface CheckFsService {

    fun checkFs(dto: CheckRq): ResponseDto
}

@Service
class CheckFsServiceImpl(private val fsDao: FsDao,
                         private val eiDao: EiDao) : CheckFsService {

    override fun checkFs(dto: CheckRq): ResponseDto {
        val breakdownsRq = dto.planning.budget.budgetBreakdown
        checkBudgetBreakdownCurrency(breakdownsRq)
        val cpIds = breakdownsRq.asSequence().map { getCpIdFromOcId(it.id) }.toSet()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(ErrorType.FS_NOT_FOUND)
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
            val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
            val ei = toObject(Ei::class.java, eiEntity.jsonData)
            mainProcurementCategory = ei.tender.mainProcurementCategory
            checkCPV(ei, dto)
            buyers.add(ei.buyer)
            breakdownsRq.forEach { br ->
                val fs = fsMap[br.id] ?: throw ErrorException(ErrorType.FS_NOT_FOUND)
                if (fs.planning.budget.isEuropeanUnionFunded) isEuropeanUnionFunded = true
                totalAmount +=  br.amount.amount
                checkFsStatus(fs)
                checkFsAmount(fs, br)
                checkFsCurrency(fs, br)
                fs.funder?.let { funders.add(it) }
                fs.payer.let { payers.add(it) }
                breakdownsRs.add(getBudgetBreakdown(br, fs))
            }
        }

        return ResponseDto(true, null,
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
                        tender = TenderCheckRs(mainProcurementCategory = mainProcurementCategory),
                        funder = funders,
                        payer = payers,
                        buyer = buyers)
        )
    }

    private fun checkBudgetBreakdownCurrency(budgetBreakdown: List<BudgetBreakdownCheckRq>) {
        if (budgetBreakdown.asSequence().map { it.amount.currency }.toSet().size > 1) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkCPV(ei: Ei, dto: CheckRq) {
        val eiCPV = ei.tender.classification.id
        val dtoCPV = dto.tender.classification.id
        if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase()) throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun getBudgetBreakdown(breakdownRq: BudgetBreakdownCheckRq, fs: Fs): BudgetBreakdownCheckRs {
        return BudgetBreakdownCheckRs(
                id = fs.ocid,
                description = fs.planning.budget.description,
                period = fs.planning.budget.period,
                amount = breakdownRq.amount,
                europeanUnionFunding = fs.planning.budget.europeanUnionFunding,
                sourceParty = CheckSourceParty(id = fs.planning.budget.sourceEntity.id, name = fs.planning.budget.sourceEntity.name)
        )
    }

    private fun checkFsCurrency(fs: Fs, br: BudgetBreakdownCheckRq) {
        val fsCurrency = fs.planning.budget.amount.currency
        val brCurrency = br.amount.currency
        if (fsCurrency != brCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkFsAmount(fs: Fs, br: BudgetBreakdownCheckRq) {
        val brAmount = br.amount.amount
        val fsAmount = fs.planning.budget.amount.amount
        if (brAmount > fsAmount) throw ErrorException(ErrorType.INVALID_AMOUNT)
    }

    private fun checkFsStatus(fs: Fs) {
        val fsStatus = fs.tender.status
        val fsStatusDetails = fs.tender.statusDetails
        if (fsStatusDetails != TenderStatusDetails.EMPTY) throw ErrorException(ErrorType.INVALID_STATUS)
        if (!((fsStatus == TenderStatus.ACTIVE || fsStatus == TenderStatus.PLANNING || fsStatus == TenderStatus.PLANNED)))
            throw ErrorException(ErrorType.INVALID_STATUS)
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf("-FS-")
        return ocId.substring(0, pos)
    }

}
