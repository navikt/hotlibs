CREATE TABLE test
(
    id   BIGSERIAL NOT NULL,
    name TEXT      NOT NULL,
    age  INTEGER   NOT NULL,
    data JSONB     NOT NULL,

    PRIMARY KEY (id)
);

INSERT INTO test(name, age, data)
VALUES ('one', 10, '{ "value": "one" }' FORMAT JSON),
       ('two', 15, '{ "value": "two" }' FORMAT JSON),
       ('three', 20, '{ "value": "three" }' FORMAT JSON),
       ('four', 25, '{ "value": "four" }' FORMAT JSON),
       ('five', 30, '{ "value": "five" }' FORMAT JSON);
