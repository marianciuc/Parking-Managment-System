CREATE TABLE IF NOT EXISTS notification_consumers
(
    id                           UUID PRIMARY KEY,                      -- AbstractBaseEntity ID
    creation_date                TIMESTAMP   NOT NULL,                  -- AbstractBaseEntity created_at
    modification_date            TIMESTAMP   NOT NULL,                  -- AbstractBaseEntity updated_at
    version                      INT         NOT NULL DEFAULT 0,        -- AbstractBaseEntity version
    record_status                VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- AbstractBaseEntity status
    expo_push_notification_token varchar(255),
    user_id                      uuid,
    is_subscribed                boolean
);

