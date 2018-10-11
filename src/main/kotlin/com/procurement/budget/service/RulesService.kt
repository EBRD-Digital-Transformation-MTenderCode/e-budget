package com.procurement.budget.service

import com.procurement.budget.dao.RulesDao
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import org.springframework.stereotype.Service

@Service
class RulesService(private val rulesDao: RulesDao) {

    fun getCpvCodeRegex(country: String): String {
        return rulesDao.getValue(country, PARAMETER_CPV) ?: throw ErrorException(ErrorType.RULES_NOT_FOUND)
    }

    companion object {
        private const val PARAMETER_CPV = "cpv"
    }
}
