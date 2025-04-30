CREATE TABLE account_balance_stats
(
    id                 UUID                        NOT NULL,
    creation_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_date  TIMESTAMP WITHOUT TIME ZONE,
    record_status      varchar(50)                 NOT NULL,
    version            INTEGER                     NOT NULL,
    account_balance_id UUID                        NOT NULL,
    current_balance    DECIMAL                     NOT NULL,
    blocked_amount     DECIMAL                     NOT NULL,
    withdrawn_amount   DECIMAL                     NOT NULL,
    CONSTRAINT pk_account_balance_stats PRIMARY KEY (id)
);

CREATE TABLE account_balances
(
    id                  UUID                        NOT NULL,
    creation_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_date   TIMESTAMP WITHOUT TIME ZONE,
    record_status       varchar(50)                 NOT NULL,
    version             INTEGER                     NOT NULL,
    system_currency     SMALLINT,
    preferred_currency  VARCHAR(255)                NOT NULL,
    balance             DECIMAL                     NOT NULL,
    owener_id           UUID                        NOT NULL,
    account_type        VARCHAR(255)                NOT NULL,
    stripe_account_id   VARCHAR(255),
    is_verified_account BOOLEAN,
    CONSTRAINT pk_account_balances PRIMARY KEY (id)
);

CREATE TABLE transactions
(
    id                     UUID                        NOT NULL,
    creation_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_date      TIMESTAMP WITHOUT TIME ZONE,
    record_status          varchar(50)                 NOT NULL,
    version                INTEGER                     NOT NULL,
    stripe_payment_id      varchar(200),
    source_account_id      UUID,
    subscription_order_id  UUID,
    session_id             UUID,
    parking_id             UUID,
    source_amount          DECIMAL,
    source_currency        varchar(50),
    destination_amount     DECIMAL,
    destination_currency   varchar(50),
    failure_message        TEXT,
    converstation_rate     DECIMAL,
    currency_date          TIMESTAMP,
    destination_account_id UUID,
    status                 VARCHAR(255)                NOT NULL,
    type                   VARCHAR(255)                NOT NULL,
    bank_account_id        UUID,
    CONSTRAINT pk_transactions PRIMARY KEY (id)
);

CREATE TABLE bank_accounts
(
    id                 UUID                        NOT NULL,
    creation_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_date  TIMESTAMP WITHOUT TIME ZONE,
    record_status      varchar(50)                 NOT NULL,
    version            INTEGER                     NOT NULL,
    account_balance_id UUID                        NOT NULL,
    iban               VARCHAR(34)                 NOT NULL,
    pm_id              VARCHAR(255),
    country            varchar(2),
    fullname           varchar(255),
    bank_name          VARCHAR(255),
    CONSTRAINT pk_bank_accounts PRIMARY KEY (id)
);

ALTER TABLE account_balances
    ADD CONSTRAINT uc_account_balances_owener UNIQUE (owener_id);

ALTER TABLE account_balance_stats
    ADD CONSTRAINT FK_ACCOUNT_BALANCE_STATS_ON_ACCOUNT_BALANCE FOREIGN KEY (account_balance_id) REFERENCES account_balances (id);

ALTER TABLE bank_accounts
    ADD CONSTRAINT FK_BANK_ACCOUNTS_ON_ACCOUNT_BALANCE FOREIGN KEY (account_balance_id) REFERENCES account_balances (id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_BANK_ACCOUNT FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_DESTINATION_ACCOUNT FOREIGN KEY (destination_account_id) REFERENCES account_balances (id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_SOURCE_ACCOUNT FOREIGN KEY (source_account_id) REFERENCES account_balances (id);