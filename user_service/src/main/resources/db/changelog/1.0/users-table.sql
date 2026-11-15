--liquibase formatted sql
--changeset Andr33w:PTM-003
--logicalFilePath:1.0/users-table.sql

CREATE TABLE IF NOT EXISTS users (
id          UUID            PRIMARY KEY     DEFAULT uuidv7(),
name        TEXT,
last_name   TEXT,
email       TEXT            NOT NULL        UNIQUE,
phone       TEXT            NOT NULL        UNIQUE,
birth_date  TIMESTAMP       DEFAULT NOW(),
created_at  TIMESTAMPTZ     DEFAULT NOW(),
updated_at  TIMESTAMPTZ     DEFAULT NOW()

);

--rollback DROP TABLE USERS;