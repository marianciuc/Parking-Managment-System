create TABLE if not exists administrators
(
    id                      UUID                        NOT NULL,
    creation_date           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_date       TIMESTAMP WITHOUT TIME ZONE,
    record_status           varchar(50)                 NOT NULL,
    version                 INTEGER                     NOT NULL,
    firstname               varchar(200),
    lastname                varchar(200),
    profile_image_url       varchar(255),
    phone_numebr            varchar(15),
    position                varchar(50),
    hire_date               DATE,
    termination_date        DATE,
    date_of_birth           DATE,
    system_user_id          uuid                        not null,
    is_active_administrator boolean                     not null
)