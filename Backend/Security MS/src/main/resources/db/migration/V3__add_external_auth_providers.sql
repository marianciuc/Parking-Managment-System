-- Create the api_keys table
CREATE TABLE external_authentication_providers
(
    id                UUID PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL,
    modification_date TIMESTAMP,
    version           INT          NOT NULL DEFAULT 1,
    record_status     VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    issuer            varchar(255) NOT NULL,
    subject_id        varchar(255) NOT NULL,
    user_id           uuid references users (id)
);

ALTER TABLE users add column is_supporting_login_by_credentials boolean;
ALTER TABLE users add column is_supported_external_authentication_provider boolean;
