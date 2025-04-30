CREATE TABLE IF NOT EXISTS gates
(
    id                UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER     NOT NULL DEFAULT 0,                 -- From AbstractBaseEntity
    parking_id        uuid references parking (id),
    host              varchar(255),
    port              int,
    lastUpdate        timestamp without time zone,
    is_manual_mode    boolean,
    status            varchar(50),
    type              varchar(50)
)