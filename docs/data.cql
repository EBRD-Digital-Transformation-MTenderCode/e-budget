CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.budget_ei (
    cp_id text,
    token_entity uuid,
    owner text,
    created_date timestamp,
    json_data text,
    primary key(cp_id, token_entity)
);

CREATE TABLE IF NOT EXISTS  ocds.budget_fs (
    cp_id text,
    oc_id text,
    token_entity uuid,
    owner text,
    amount double,
    amount_reserved double,
    created_date timestamp,
    json_data text,
    primary key(cp_id, oc_id, token_entity)
);


SELECT * FROM ocds.budget_ei;
SELECT * FROM ocds.budget_fs;

TRUNCATE ocds.budget_fs;
TRUNCATE ocds.budget_ei;
