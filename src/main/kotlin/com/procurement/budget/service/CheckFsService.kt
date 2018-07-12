package com.procurement.budget.service

import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.CheckBudgetBreakdown
import com.procurement.budget.model.dto.check.CheckRequest
import com.procurement.budget.model.dto.check.CheckResponse
import com.procurement.budget.model.dto.check.CheckSourceParty
import com.procurement.budget.model.dto.ei.Ei
import com.procurement.budget.model.dto.ei.OrganizationReferenceEi
import com.procurement.budget.model.dto.fs.Fs
import com.procurement.budget.model.dto.fs.OrganizationReferenceFs
import com.procurement.budget.model.dto.ocds.TenderStatus
import com.procurement.budget.model.dto.ocds.TenderStatusDetails
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service
import java.util.*

interface CheckFsService {

    fun checkFs(dto: CheckRequest): ResponseDto
}

@Service
class CheckFsServiceImpl(private val fsDao: FsDao,
                         private val eiDao: EiDao) : CheckFsService {

    override fun checkFs(dto: CheckRequest): ResponseDto {
        val budgetBreakdowns = dto.budgetBreakdown
        checkBudgetBreakdownCurrency(budgetBreakdowns)
        val cpIds = budgetBreakdowns.asSequence().map { getCpIdFromOcId(it.id) }.toSet()
        val funders = HashSet<OrganizationReferenceFs>()
        val payers = HashSet<OrganizationReferenceFs>()
        val buyers = HashSet<OrganizationReferenceEi>()
        val entities = fsDao.getAllByCpIds(cpIds)
        if (entities.isEmpty()) throw ErrorException(ErrorType.FS_NOT_FOUND)
        val fsMap = HashMap<String?, Fs>()
        entities.asSequence()
                .map { toObject(Fs::class.java, it.jsonData) }
                .forEach { fsMap[it.ocid] = it }

        for (cpId in cpIds) {
            val eiEntity = eiDao.getByCpId(cpId) ?: throw ErrorException(ErrorType.EI_NOT_FOUND)
            val ei = toObject(Ei::class.java, eiEntity.jsonData)
            checkCPV(ei, dto)
            buyers.add(ei.buyer)
            budgetBreakdowns.forEach { br ->
                val fs = fsMap[br.id] ?: throw ErrorException(ErrorType.FS_NOT_FOUND)
                checkFsStatus(fs)
                checkFsAmount(fs, br)
                checkFsCurrency(fs, br)
                processBudgetBreakdown(br, fs)
                fs.funder?.let { funders.add(it) }
                fs.payer.let { payers.add(it) }
            }
        }

        return ResponseDto(true, null,
                CheckResponse(
                        ei = cpIds,
                        budgetBreakdown = budgetBreakdowns,
                        funder = funders,
                        payer = payers,
                        buyer = buyers)
        )
    }

    private fun checkBudgetBreakdownCurrency(budgetBreakdown: List<CheckBudgetBreakdown>) {
        if (budgetBreakdown.asSequence().map { it.amount.currency }.toSet().size > 1) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkCPV(ei: Ei, dto: CheckRequest) {
        val eiCPV = ei.tender.classification.id
        val dtoCPV = dto.classification.id
        if (eiCPV.substring(0, 3).toUpperCase() != dtoCPV.substring(0, 3).toUpperCase())
            throw ErrorException(ErrorType.INVALID_CPV)
    }

    private fun processBudgetBreakdown(br: CheckBudgetBreakdown, fs: Fs) {
        val fsSe = fs.planning.budget.sourceEntity
        br.sourceParty = CheckSourceParty(id = fsSe.id, name = fsSe.name)
        br.period = fs.planning.budget.period
    }

    private fun checkFsCurrency(fs: Fs, br: CheckBudgetBreakdown) {
        val fsCurrency = fs.planning.budget.amount.currency
        val brCurrency = br.amount.currency
        if (fsCurrency != brCurrency) throw ErrorException(ErrorType.INVALID_CURRENCY)
    }

    private fun checkFsAmount(fs: Fs, br: CheckBudgetBreakdown) {
        val fsAmount = fs.planning.budget.amount.amount
        val brAmount = br.amount.amount
        if (brAmount.compareTo(fsAmount) == 1) throw ErrorException(ErrorType.INVALID_AMOUNT)
    }

    private fun checkFsStatus(fs: Fs) {
        val fsStatus = fs.tender.status
        val fsStatusDetails = fs.tender.statusDetails
        if (!((fsStatus == TenderStatus.ACTIVE || fsStatus == TenderStatus.PLANNING || fsStatus == TenderStatus.PLANNED)
                        && fsStatusDetails == TenderStatusDetails.EMPTY))
            throw ErrorException(ErrorType.INVALID_STATUS)
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf("-FS-")
        return ocId.substring(0, pos)
    }

}
