CREATE TABLE IF NOT EXISTS sessions
(
    id                  UUID         NOT NULL PRIMARY KEY,
    creation_date       TIMESTAMP    NOT NULL,
    modification_date   TIMESTAMP,
    record_status       VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    version             INT          NOT NULL,

    vehicle_id          uuid         NOT NULL,
    plate_number        varchar(255) not null,
    vehicle_access_list varchar(255),
    parking_id          uuid         not null,
    start_time          TIMESTAMP,
    end_time            TIMESTAMP,
    paid_until          TIMESTAMP,
    owner_id            uuid,
    price               DECIMAL(10, 2),
    tariff_id           UUID,
    tariff_name         VARCHAR(255),
    status              VARCHAR(20)  NOT NULL,
    created_at          TIMESTAMP             DEFAULT now(),
    updated_at          TIMESTAMP             DEFAULT now(),


    CONSTRAINT valid_record_status CHECK (record_status IN ('ACTIVE', 'DELETED'))
);

CREATE INDEX idx_sessions_plate_number ON sessions (plate_number);

ALTER TABLE sessions
    ADD CONSTRAINT chk_session_status CHECK (status IN (
                                                        'PREPARE',
                                                        'ACTIVE',
                                                        'FINISHED',
                                                        'PREPARE_END',
                                                        'CANCELLED',
                                                        'STOPPED_BY_OWNER'
        ));
