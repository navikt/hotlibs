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
VALUES ('one', 10, 'MALE', '{ "value": "one" }'),
       ('two', 15, 'FEMALE', '{ "value": "two" }'),
       ('three', 20, 'FEMALE', '{ "value": "three" }'),
       ('four', 25, 'MALE', '{ "value": "four" }'),
       ('five', 30, 'FEMALE', '{ "value": "five" }');

CREATE TABLE json
(
    id   BIGSERIAL NOT NULL,
    data JSONB     NULL,

    PRIMARY KEY (id)
);
