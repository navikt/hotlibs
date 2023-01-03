CREATE TABLE person
(
    id     BIGSERIAL NOT NULL,
    name   TEXT      NOT NULL,
    age    INTEGER   NOT NULL,
    gender TEXT      NOT NULL,
    data   JSONB     NOT NULL,

    PRIMARY KEY (id)
);

INSERT INTO person(name, age, gender, data)
VALUES ('one', 10, 'MALE', '{ "value": "one" }' FORMAT JSON),
       ('two', 15, 'FEMALE', '{ "value": "two" }' FORMAT JSON),
       ('three', 20, 'FEMALE', '{ "value": "three" }' FORMAT JSON),
       ('four', 25, 'MALE', '{ "value": "four" }' FORMAT JSON),
       ('five', 30, 'FEMALE', '{ "value": "five" }' FORMAT JSON);
