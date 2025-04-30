-- Create the api_keys table
CREATE TABLE api_keys
(
    id                UUID PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL,
    modification_date TIMESTAMP,
    version           INT          NOT NULL DEFAULT 1,
    record_status     VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    key_value         VARCHAR(255) NOT NULL UNIQUE,
    parking_id        UUID         NOT NULL,
    issued_by         UUID,
    revoked_by        UUID,
    status            VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE'
);

-- Create the api_key_scopes table (element collection for scope)
CREATE TABLE api_key_scopes
(
    key_id UUID        NOT NULL REFERENCES api_keys (id) ON DELETE CASCADE,
    scope  VARCHAR(50) NOT NULL,
    PRIMARY KEY (key_id, scope)
);
