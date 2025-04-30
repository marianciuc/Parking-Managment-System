CREATE TABLE IF NOT EXISTS vehicles
(
    id                UUID         NOT NULL PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP,
    record_status     VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    version           INT          NOT NULL DEFAULT 1,
    brand             VARCHAR(255),
    model             VARCHAR(255),
    color             VARCHAR(50),
    year              INT,
    vin               VARCHAR(255),
    vehicle_class     VARCHAR(255),
    plate_number      VARCHAR(255) NOT NULL,
    image_url         VARCHAR(255),
    has_owner         BOOLEAN      NOT NULL DEFAULT FALSE,
    ownership_id      UUID
);

CREATE INDEX plate_number_index ON vehicles (plate_number);
CREATE INDEX idx_vehicles_vin ON vehicles (vin);

CREATE TABLE IF NOT EXISTS ownerships
(
    id                UUID         NOT NULL PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP,
    record_status     VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    version           INT          NOT NULL DEFAULT 1,
    vehicle_id        UUID         NOT NULL,
    owner_id          UUID         NOT NULL,
    owner_type        VARCHAR(255) NOT NULL,
    constraint fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id)
);

CREATE INDEX vehicle_id_index ON ownerships (vehicle_id);
CREATE INDEX owner_id_index ON ownerships (owner_id);

ALTER TABLE vehicles
    ADD CONSTRAINT fk_vehicles_ownerships
        FOREIGN KEY (ownership_id)
            REFERENCES ownerships (id);

CREATE TABLE IF NOT EXISTS ownership_change_requests
(
    id                UUID         NOT NULL PRIMARY KEY,
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP,
    record_status     VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    version           INT          NOT NULL DEFAULT 1,
    vehicle_id        UUID         NOT NULL,
    owner_id          uuid         NOT NULL,
    initiator_id      UUID         NOT NULL,
    initiator_type    VARCHAR(255) NOT NULL,
    status            VARCHAR(255) NOT NULL,
    reaction_date     TIMESTAMP,
    reacted_by        UUID,
    CONSTRAINT fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED', 'COMPLETED'))
);