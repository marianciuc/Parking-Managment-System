CREATE TABLE tariffs
(
    id                UUID      NOT NULL PRIMARY KEY, -- Primary key from AbstractBaseEntity

    -- AbstractBaseEntity fields
    creation_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    record_status     VARCHAR(50)        DEFAULT 'ACTIVE',
    version           INT       NOT NULL,

    -- Tariff-specific fields
    parking_id        UUID      NOT NULL,
    name              VARCHAR(255),
    description       TEXT,
    tariff_class      VARCHAR(50),
    price             NUMERIC(19, 4),
    currency          VARCHAR(10)
);