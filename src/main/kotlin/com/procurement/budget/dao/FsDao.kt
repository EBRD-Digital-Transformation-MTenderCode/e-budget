package com.procurement.budget.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.budget.model.entity.FsEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*


interface FsDao {

    fun save(entity: FsEntity)

    fun getByCpIdAndToken(cpId: String, token: UUID): FsEntity?

    fun getAllByCpId(cpId: String): List<FsEntity>

    fun getAllByCpIds(cpIds: Set<String>): List<FsEntity>

    fun getTotalAmountByCpId(cpId: String): BigDecimal?
}

@Service
class FsDaoImpl(private val session: Session) : FsDao {

    override fun save(entity: FsEntity) {
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

    override fun getByCpIdAndToken(cpId: String, token: UUID): FsEntity? {
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

    override fun getAllByCpId(cpId: String): List<FsEntity> {
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

    override fun getAllByCpIds(cpIds: Set<String>): List<FsEntity> {
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

    override fun getTotalAmountByCpId(cpId: String): BigDecimal? {
        val query = select().sum(AMOUNT).`as`(AMOUNT)
                .from(FS_TABLE)
                .where(eq(CP_ID, cpId))
        val row = session.execute(query).one()
        return row?.getDecimal(AMOUNT)
    }

    companion object {

        private val FS_TABLE = "budget_fs"
        private val CP_ID = "cp_id"
        private val OC_ID = "oc_id"
        private val TOKEN = "token_entity"
        private val OWNER = "owner"
        private val AMOUNT = "amount"
        private val AMOUNT_RESERVED = "amount_reserved"
        private val CREATED_DATE = "created_date"
        private val JSON_DATA = "json_data"
    }
}
