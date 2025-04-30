CREATE TABLE IF NOT EXISTS notifications
(
    id                UUID PRIMARY KEY,                       -- AbstractBaseEntity ID
    creation_date     TIMESTAMP    NOT NULL,                  -- AbstractBaseEntity created_at
    modification_date TIMESTAMP    NOT NULL,                  -- AbstractBaseEntity updated_at
    version           INT          NOT NULL DEFAULT 0,        -- AbstractBaseEntity version
    record_status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE', -- AbstractBaseEntity status
    title             VARCHAR(255) NOT NULL,                  -- Notification title
    message           TEXT         NOT NULL,                  -- Notification message
    receiver_id       UUID         NOT NULL,                  -- Notification receiver ID
    is_read           BOOLEAN      NOT NULL DEFAULT FALSE     -- Notification read status
);

