CREATE TABLE owners
(
    id                     UUID PRIMARY KEY,
    user_id                UUID UNIQUE,
    first_name             VARCHAR(255),
    middle_name            VARCHAR(255),
    last_name              VARCHAR(255),
    nip                    VARCHAR(255) UNIQUE,
    company_name           VARCHAR(255),
    date_of_birth          DATE,
    owner_type             VARCHAR(50),
    phone_number           VARCHAR(20),
    notes                  TEXT,
    phone_number_code      VARCHAR(10),
    address_id             UUID,
    registration_completed BOOLEAN      NOT NULL,
    is_deleted             BOOLEAN      NOT NULL,
    creation_date           TIMESTAMP,
    modification_date     TIMESTAMP
);

CREATE INDEX idx_owners_nip ON owners(nip);
CREATE INDEX idx_owners_id ON owners(id);
CREATE INDEX idx_owners_address_id ON owners(address_id);
