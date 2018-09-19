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
    amount decimal,
    amount_reserved decimal,
    created_date timestamp,
    json_data text,
    primary key(cp_id, token_entity)
);

CREATE TABLE IF NOT EXISTS  ocds.budget_rules (
    country text,
    parameter text,
    value text,
    primary key(country, parameter)
);

CREATE TABLE IF NOT EXISTS  ocds.budget_history (
    operation_id text,
    command text,
    operation_date timestamp,
    json_data text,
    primary key(operation_id, command)
);

INSERT INTO ocds.budget_rules (country, parameter, value)  VALUES ('MD', 'cpv', '^[0-9]{2}[1-9][0]{5}-[0-9]$');
INSERT INTO ocds.budget_rules (country, parameter, value)  VALUES ('TEST', 'cpv', '^[0-9]{2}[1-9][0]{5}-[0-9]$');

^\d[1-9]\d[1-9]\d[1-9]\d[0]\d[0]\d[0]\d[0]-\d[1-9]$