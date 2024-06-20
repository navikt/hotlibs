CREATE TABLE test
(
    id      BIGSERIAL NOT NULL,
    string  TEXT      NOT NULL,
    integer INTEGER   NOT NULL,
    enum    TEXT      NOT NULL,
    data_1   JSONB    NOT NULL,
    data_2   JSONB    NULL,

    PRIMARY KEY (id)
);
