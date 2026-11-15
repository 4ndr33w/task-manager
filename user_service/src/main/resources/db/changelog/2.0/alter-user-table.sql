--liquibase formatted sql
--changeset Andr33w:PTM-004-alter-users-table
--logicalFilePath:2.0/alter-user-table.sql

ALTER TABLE users
ADD COLUMN password TEXT;

--rollback ALTER TABLE users DROP COLUMN password;