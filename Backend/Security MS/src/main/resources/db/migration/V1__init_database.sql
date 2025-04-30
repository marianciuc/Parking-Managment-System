CREATE TABLE users
(
    id                UUID PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL,
    modification_date TIMESTAMP,
    version           INT          NOT NULL DEFAULT 1,
    record_status     VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    email             VARCHAR(255) NOT NULL UNIQUE,
    password_hash     VARCHAR(255),
    user_type         VARCHAR(50)  NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);


CREATE TABLE user_possibilities
(
    user_id     UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    possibility VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, possibility)
);