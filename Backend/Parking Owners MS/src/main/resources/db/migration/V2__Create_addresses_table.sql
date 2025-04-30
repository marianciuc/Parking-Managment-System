CREATE TABLE addresses
(
    id                 UUID PRIMARY KEY,
    city               VARCHAR(100) NOT NULL,
    region             VARCHAR(100),
    street             VARCHAR(200) NOT NULL,
    house_number       VARCHAR(20)  NOT NULL,
    apartment_number   VARCHAR(20),
    postal_code        VARCHAR(10)  NOT NULL,
    country            VARCHAR(100) NOT NULL,
    additional_info    TEXT,
    owner_id           UUID UNIQUE NOT NULL,
    creation_date       TIMESTAMP,
    modification_date TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES owners (id) ON DELETE CASCADE
);

ALTER TABLE owners ADD CONSTRAINT fk_address_id FOREIGN KEY (address_id) REFERENCES addresses (id) ON DELETE CASCADE;

CREATE INDEX idx_addresses_postal_code ON addresses(postal_code);
CREATE INDEX idx_addresses_owner_id ON addresses(owner_id);
