CREATE TABLE task
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name        VARCHAR(255)                NOT NULL,
    description TEXT,
    status      varchar(40)                 not null
);