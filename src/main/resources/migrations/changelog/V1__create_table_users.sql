CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    email    TEXT NOT NULL UNIQUE,
    password TEXT    NOT NULL,
    role     TEXT,
    CONSTRAINT uc_email UNIQUE (email)
);