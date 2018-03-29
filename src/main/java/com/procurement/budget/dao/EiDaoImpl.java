package com.procurement.budget.dao;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.procurement.budget.model.entity.EiEntity;
import java.util.UUID;
import org.springframework.stereotype.Service;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

@Service
public class EiDaoImpl implements EiDao {

    private static final String EI_TABLE = "budget_ei";
    private static final String CP_ID = "cp_id";
    private static final String TOKEN = "token_entity";
    private static final String OWNER = "owner";
    private static final String CREATED_DATE = "created_date";
    private static final String JSON_DATA = "json_data";

    private final Session session;

    public EiDaoImpl(final Session session) {
        this.session = session;
    }

    @Override
    public void save(final EiEntity entity) {
        final Insert insert = insertInto(EI_TABLE);
        insert.value(CP_ID, entity.getCpId())
                .value(TOKEN, entity.getToken())
                .value(OWNER, entity.getOwner())
                .value(CREATED_DATE, entity.getCreatedDate())
                .value(JSON_DATA, entity.getJsonData());
        session.execute(insert);
    }

    @Override
    public EiEntity getByCpIdAndToken(final String cpId, final UUID token) {
        final Statement query = select()
                .all()
                .from(EI_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(TOKEN, token)).limit(1);
        final Row row = session.execute(query).one();
        if (row != null)
            return new EiEntity(
                    row.getString(CP_ID),
                    row.getUUID(TOKEN),
                    row.getString(OWNER),
                    row.getTimestamp(CREATED_DATE),
                    row.getString(JSON_DATA));
        return null;
    }

    @Override
    public EiEntity getByCpId(final String cpId) {
        final Statement query = select()
                .all()
                .from(EI_TABLE)
                .where(eq(CP_ID, cpId)).limit(1);
        final Row row = session.execute(query).one();
        if (row != null)
            return new EiEntity(
                    row.getString(CP_ID),
                    row.getUUID(TOKEN),
                    row.getString(OWNER),
                    row.getTimestamp(CREATED_DATE),
                    row.getString(JSON_DATA));
        return null;
    }
}
