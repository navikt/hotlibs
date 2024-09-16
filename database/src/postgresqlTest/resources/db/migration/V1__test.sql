CREATE TABLE test
(
    id       BIGSERIAL NOT NULL,
    string   TEXT      NOT NULL,
    integer  INTEGER   NOT NULL,
    enum     TEXT      NOT NULL,
    data_1   jsonb     NOT NULL,
    data_2   jsonb     NULL,
    fnr      CHAR(11)  NULL,
    aktor_id CHAR(13)  NULL,

    PRIMARY KEY (id)
);
