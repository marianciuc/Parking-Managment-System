CREATE TABLE IF NOT EXISTS reviews
(
    id                UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER     NOT NULL DEFAULT 0,                 -- From AbstractBaseEntity
    review_text       TEXT        NOT NULL,
    rating            INTEGER     NOT NULL,
    author_id         UUID        NOT NULL,
    parking_id        UUID        NOT NULL,
    moderation_status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
)