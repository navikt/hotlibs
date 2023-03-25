CREATE TABLE test_1
(
    id      BIGSERIAL NOT NULL,
    string  TEXT      NOT NULL,
    integer INTEGER   NOT NULL,
    enum    TEXT      NOT NULL,
    data_1   JSONB    NOT NULL,
    data_2   JSONB    NULL,

    PRIMARY KEY (id)
);

CREATE TABLE test_2
(
    id        BIGSERIAL NOT NULL,
    test_1_id BIGINT REFERENCES test_1 ON DELETE CASCADE,
    boolean   BOOLEAN,
    instant   TIMESTAMPTZ DEFAULT (NOW() AT TIME ZONE 'UTC'),

    PRIMARY KEY (id)
);
