CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE usr
(
    id         BIGINT       NOT NULL PRIMARY KEY DEFAULT nextval('USER_SEQ'::regclass),
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(50)  NOT NULL UNIQUE,
    activated  BOOLEAN      NOT NULL
);

ALTER SEQUENCE USER_SEQ OWNED BY usr.id;
create table user_authority
(
    user_id   BIGINT      NOT NULL,

    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES usr (id)
)

