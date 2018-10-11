package com.procurement.budget.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.budget.model.entity.FsEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class FsDao(private val session: Session) {

    fun save(entity: FsEntity) {
        val insert = insertInto(FS_TABLE)
        insert.value(CP_ID, entity.cpId)
                .value(OC_ID, entity.ocId)
                .value(TOKEN, entity.token)
                .value(OWNER, entity.owner)
                .value(AMOUNT, entity.amount)
                .value(AMOUNT_RESERVED, entity.amountReserved)
                .value(CREATED_DATE, entity.createdDate)
                .value(JSON_DATA, entity.jsonData)
        session.execute(insert)
    }

    fun getByCpIdAndToken(cpId: String, token: UUID): FsEntity? {
        val query = select()
                .all()
                .from(FS_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(TOKEN, token))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) FsEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getUUID(TOKEN),
                row.getString(OWNER),
                row.getDecimal(AMOUNT),
                row.getDecimal(AMOUNT_RESERVED),
                row.getTimestamp(CREATED_DATE),
                row.getString(JSON_DATA)) else null
    }

    fun getAllByCpId(cpId: String): List<FsEntity> {
        val query = select()
                .all()
                .from(FS_TABLE)
                .where(eq(CP_ID, cpId))
        val resultSet = session.execute(query)
        val entities = ArrayList<FsEntity>()
        resultSet.forEach { row ->
            entities.add(FsEntity(
                    row.getString(CP_ID),
                    row.getString(OC_ID),
                    row.getUUID(TOKEN),
                    row.getString(OWNER),
                    row.getDecimal(AMOUNT),
                    row.getDecimal(AMOUNT_RESERVED),
                    row.getTimestamp(CREATED_DATE),
                    row.getString(JSON_DATA)))
        }
        return entities
    }

    fun getAllByCpIds(cpIds: Set<String>): List<FsEntity> {
        val query = select()
                .all()
                .from(FS_TABLE)
                .where(`in`(CP_ID, *cpIds.toTypedArray()))
        val resultSet = session.execute(query)
        val entities = ArrayList<FsEntity>()
        resultSet.forEach { row ->
            entities.add(FsEntity(
                    row.getString(CP_ID),
                    row.getString(OC_ID),
                    row.getUUID(TOKEN),
                    row.getString(OWNER),
                    row.getDecimal(AMOUNT),
                    row.getDecimal(AMOUNT_RESERVED),
                    row.getTimestamp(CREATED_DATE),
                    row.getString(JSON_DATA)))
        }
        return entities
    }

    fun getTotalAmountByCpId(cpId: String): BigDecimal? {
        val query = select().sum(AMOUNT).`as`(AMOUNT)
                .from(FS_TABLE)
                .where(eq(CP_ID, cpId))
        val row = session.execute(query).one()
        return row?.getDecimal(AMOUNT)
    }

    fun getCountByCpId(cpId: String): Long {
        val query = select().countAll()
                .from(FS_TABLE)
                .where(eq(CP_ID, cpId))
        val row = session.execute(query).one()
        return row?.getLong(1) ?: 0L
    }

    companion object {

        private const val FS_TABLE = "budget_fs"
        private const val CP_ID = "cp_id"
        private const val OC_ID = "oc_id"
        private const val TOKEN = "token_entity"
        private const val OWNER = "owner"
        private const val AMOUNT = "amount"
        private const val AMOUNT_RESERVED = "amount_reserved"
        private const val CREATED_DATE = "created_date"
        private const val JSON_DATA = "json_data"
    }
}
