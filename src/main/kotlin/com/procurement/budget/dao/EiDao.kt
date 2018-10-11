package com.procurement.budget.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.budget.model.entity.EiEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class EiDao(private val session: Session) {

    fun save(entity: EiEntity) {
        val insert = insertInto(EI_TABLE)
        insert.value(CP_ID, entity.cpId)
                .value(TOKEN, entity.token)
                .value(OWNER, entity.owner)
                .value(CREATED_DATE, entity.createdDate)
                .value(JSON_DATA, entity.jsonData)
        session.execute(insert)
    }

    fun getByCpIdAndToken(cpId: String, token: UUID): EiEntity? {
        val query = select()
                .all()
                .from(EI_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(TOKEN, token)).limit(1)
        val row = session.execute(query).one()
        return if (row != null) EiEntity(
                row.getString(CP_ID),
                row.getUUID(TOKEN),
                row.getString(OWNER),
                row.getTimestamp(CREATED_DATE),
                row.getString(JSON_DATA)) else null
    }

    fun getByCpId(cpId: String): EiEntity? {
        val query = select()
                .all()
                .from(EI_TABLE)
                .where(eq(CP_ID, cpId)).limit(1)
        val row = session.execute(query).one()
        return if (row != null) EiEntity(
                row.getString(CP_ID),
                row.getUUID(TOKEN),
                row.getString(OWNER),
                row.getTimestamp(CREATED_DATE),
                row.getString(JSON_DATA)) else null
    }

    companion object {
        private const val EI_TABLE = "budget_ei"
        private const val CP_ID = "cp_id"
        private const val TOKEN = "token_entity"
        private const val OWNER = "owner"
        private const val CREATED_DATE = "created_date"
        private const val JSON_DATA = "json_data"
    }
}
