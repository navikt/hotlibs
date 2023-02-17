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

INSERT INTO test_1(string, integer, enum, data_1)
VALUES ('string1', 1, 'A', '{ "key": "value1" }'),
       ('string2', 2, 'B', '{ "key": "value2" }'),
       ('string3', 3, 'C', '{ "key": "value3" }'),
       ('string4', 4, 'A', '{ "key": "value4" }'),
       ('string5', 5, 'B', '{ "key": "value5" }');

CREATE TABLE test_2
(
    id        BIGSERIAL NOT NULL,
    test_1_id BIGINT REFERENCES test_1 ON DELETE CASCADE,
    boolean   BOOLEAN,
    instant   TIMESTAMPTZ,

    PRIMARY KEY (id)
);

INSERT INTO test_2(test_1_id, boolean, instant)
VALUES ((SELECT id FROM test_1 LIMIT 1 OFFSET 0), TRUE, NOW()),
       ((SELECT id FROM test_1 LIMIT 1 OFFSET 1), TRUE, NOW()),
       ((SELECT id FROM test_1 LIMIT 1 OFFSET 2), TRUE, NOW()),
       ((SELECT id FROM test_1 LIMIT 1 OFFSET 3), TRUE, NOW()),
       ((SELECT id FROM test_1 LIMIT 1 OFFSET 4), TRUE, NOW());
