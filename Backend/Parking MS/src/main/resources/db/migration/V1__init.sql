CREATE TABLE parking
(
    id                          UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date               TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status               VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version                     INTEGER     NOT NULL DEFAULT 0,                 -- From AbstractBaseEntity
    image_url                   VARCHAR(255),                                   -- Assuming the image URL is stored in the database
    name                        VARCHAR(255),                                   -- Assuming the name of the parking lot is stored in the database
    address_id                  UUID,                                           -- Assuming the address of the parking lot is stored in the database
    capacity_details_id         UUID,                                           -- Assuming the capacity details of the parking lot is stored in the database
    owner_id                    UUID,                                           -- Assuming the owner of the parking lot is stored in the database
    access_type                 VARCHAR(50),
    status                      VARCHAR(50),
    ip_address                  VARCHAR(255),
    parking_creation_process_id UUID,
    is_pinned                   BOOLEAN     NOT NULL DEFAULT FALSE,
    is_24h                      BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_parking_name ON parking (name);

CREATE TABLE parking_creation_processes
(
    id                UUID PRIMARY KEY,
    creation_date     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    record_status     VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    version           INTEGER     NOT NULL DEFAULT 0,
    message           TEXT,
    moderated_by      UUID,
    parking_id        UUID,
    meeting_time      TIMESTAMP,
    finished_step     INTEGER     NOT NULL DEFAULT 0,
    status            VARCHAR(50) NOT NULL,
    user_id           UUID,
    notes             TEXT,
    CONSTRAINT fk_parking FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE
);

ALTER TABLE parking
    ADD CONSTRAINT fk_parking_creation_process
        FOREIGN KEY (parking_creation_process_id)
            REFERENCES parking_creation_processes (id)
            ON DELETE CASCADE;

ALTER TABLE parking_creation_processes
    ADD CONSTRAINT check_process_status
        CHECK (status IN ('CREATED', 'PENDING', 'STARTED', 'APPROVED', 'REJECTED', 'CANCELLED', 'FINISHED'));

CREATE TABLE capacity_details
(
    id                                UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date                     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date                 TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status                     VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version                           INTEGER     NOT NULL DEFAULT 0,
    capacity                          INTEGER              DEFAULT 0,
    occupied_spaces                   INTEGER              DEFAULT 0,
    places_for_disabled               INTEGER              DEFAULT 0,
    occupied_places_for_disabled      INTEGER              DEFAULT 0,
    places_for_electric_cars          INTEGER              DEFAULT 0,
    occupied_places_for_electric_cars INTEGER              DEFAULT 0,
    parking_id                        UUID,
    CONSTRAINT fk_parking FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE
);

CREATE INDEX idx_capacity_details_parking_id ON capacity_details (parking_id);
ALTER TABLE parking
    ADD CONSTRAINT fk_capacity_details FOREIGN KEY (capacity_details_id) REFERENCES capacity_details (id) ON DELETE CASCADE;


CREATE TABLE opening_hours
(
    id                UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER     NOT NULL DEFAULT 0,
    parking_id        UUID        NOT NULL,
    day_of_week       SMALLINT    NOT NULL,
    opening_time      TIME,
    closing_time      TIME,
    is_closed         BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_parking FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE
);

ALTER TABLE opening_hours
    ADD CONSTRAINT check_day_of_week
        CHECK (day_of_week BETWEEN 0 AND 6);

CREATE INDEX idx_opening_hours_parking_id ON opening_hours (parking_id);

CREATE TABLE parking_creation_statues
(
    id                          UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date               TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status               VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version                     INTEGER     NOT NULL DEFAULT 0,
    parking_creation_process_id UUID        NOT NULL,
    message                     TEXT,
    CONSTRAINT fk_parking_creation_process FOREIGN KEY (parking_creation_process_id) REFERENCES parking_creation_processes (id) ON DELETE CASCADE
);

CREATE INDEX idx_parking_creation_statues_parking_creation_process_id ON parking_creation_statues (parking_creation_process_id);

CREATE TABLE addresses
(
    id                           UUID PRIMARY KEY,                               -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date            TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status                VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version                      INTEGER     NOT NULL DEFAULT 0,
    city                         VARCHAR(255),
    street                       VARCHAR(255),
    building_number              VARCHAR(255),
    postal_code                  VARCHAR(255),
    latitude                     DOUBLE PRECISION,
    longitude                    DOUBLE PRECISION,
    country_code                 VARCHAR(3),
    parking_id                   UUID        NOT NULL UNIQUE,
    is_belong_to_any_institution BOOLEAN     NOT NULL DEFAULT FALSE,
    institution_name             VARCHAR(255),
    CONSTRAINT fk_parking FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE
);

CREATE INDEX idx_addresses_parking_id ON addresses (parking_id);

CREATE TABLE tags
(
    id                UUID PRIMARY KEY,                                -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER      NOT NULL DEFAULT 0,
    name              VARCHAR(255) NOT NULL UNIQUE,
    description       TEXT
);

CREATE TABLE parking_tags
(
    parking_id UUID NOT NULL,         -- Foreign key referencing parking table
    tag_id     UUID NOT NULL,         -- Foreign key referencing tags table
    PRIMARY KEY (parking_id, tag_id), -- Composite key ensures no duplicate associations

    CONSTRAINT fk_parking FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);


CREATE TABLE whitelist
(
    id                UUID PRIMARY KEY,                                -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER      NOT NULL DEFAULT 0,
    plate_number      VARCHAR(255) NOT NULL,
    vehicle_id        uuid         NOT NULL,
    parking_id        uuid         NOT NULL,
    CONSTRAINT fk_vehicle FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE,
    tariff_id         uuid         NOT NULL,
    tariff_name       VARCHAR(255) NOT NULL
);

CREATE INDEX idx_whitelist_plate_number ON whitelist (plate_number);
CREATE INDEX idx_whitelist_vehicle_id ON whitelist (vehicle_id);
CREATE INDEX idx_whitelist_parking_id ON whitelist (parking_id);

CREATE TABLE blacklist
(
    id                UUID PRIMARY KEY,                                -- Assuming AbstractBaseEntity provides the 'id' field
    creation_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    modification_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- From AbstractBaseEntity
    record_status     VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',          -- From AbstractBaseEntity
    version           INTEGER      NOT NULL DEFAULT 0,
    plate_number      VARCHAR(255) NOT NULL,
    vehicle_id        uuid         NOT NULL,
    parking_id        uuid         NOT NULL,
    CONSTRAINT fk_vehicle FOREIGN KEY (parking_id) REFERENCES parking (id) ON DELETE CASCADE,
    reason            TEXT
);

CREATE INDEX idx_blacklist_plate_number ON blacklist (plate_number);
CREATE INDEX idx_blacklist_vehicle_id ON blacklist (vehicle_id);
CREATE INDEX idx_blacklist_parking_id ON blacklist (parking_id);


CREATE OR REPLACE FUNCTION update_modification_date()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modification_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- For the 'parking' table
CREATE TRIGGER before_update_parking
    BEFORE UPDATE
    ON parking
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'parking_creation_processes' table
CREATE TRIGGER before_update_parking_creation_processes
    BEFORE UPDATE
    ON parking_creation_processes
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'capacity_details' table
CREATE TRIGGER before_update_capacity_details
    BEFORE UPDATE
    ON capacity_details
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'opening_hours' table
CREATE TRIGGER before_update_opening_hours
    BEFORE UPDATE
    ON opening_hours
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'addresses' table
CREATE TRIGGER before_update_addresses
    BEFORE UPDATE
    ON addresses
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'tags' table
CREATE TRIGGER before_update_tags
    BEFORE UPDATE
    ON tags
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'whitelist' table
CREATE TRIGGER before_update_whitelist
    BEFORE UPDATE
    ON whitelist
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();

-- For the 'blacklist' table
CREATE TRIGGER before_update_blacklist
    BEFORE UPDATE
    ON blacklist
    FOR EACH ROW
EXECUTE FUNCTION update_modification_date();
