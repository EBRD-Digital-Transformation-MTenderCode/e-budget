package com.procurement.submission.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.eq
import com.datastax.driver.core.querybuilder.QueryBuilder.select
import org.springframework.stereotype.Service

interface RulesDao {

    fun getValue(country: String, parameter: String): String?

}

@Service
class RulesDaoImpl(private val session: Session) : RulesDao {

    override fun getValue(country: String, parameter: String): String? {
        val query = select()
                .column(VALUE)
                .from(RULES_TABLE)
                .where(eq(CONTRY, country))
                .and(eq(PARAMETER, parameter))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) return row.getString(VALUE)
        else null
    }

    companion object {
        private val RULES_TABLE = "budget_rules"
        private val CONTRY = "country"
        private val PARAMETER = "parameter"
        private val VALUE = "value"
    }
}