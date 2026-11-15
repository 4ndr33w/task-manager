--liquibase formatted sql
--changeset Andr33w:PTM-004-create-user-roles-table
--logicalFilePath:2.0/user-roles-table.sql

CREATE TABLE IF NOT EXISTS user_roles(
user_id UUID NOT NULL,
role TEXT NOT NULL,
PRIMARY KEY (user_id, role),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

--rollback DROP TABLE user_roles;