CREATE TYPE personnavn AS
(
    fornavn    TEXT,
    mellomnavn TEXT,
    etternavn  TEXT
);

CREATE DOMAIN gyldig_personnavn AS personnavn
    CONSTRAINT navn_domain_check CHECK ((value IS NULL) OR
                                        (((value).fornavn IS NOT NULL) AND ((value).etternavn IS NOT NULL)));

CREATE TABLE test
(
    id          BIGSERIAL         NOT NULL,

    string_1    TEXT              NOT NULL DEFAULT 'test',
    integer_1   INTEGER           NOT NULL DEFAULT 10,
    long_1      BIGINT            NOT NULL DEFAULT 20,
    enum_1      TEXT              NOT NULL DEFAULT 'A',

    data_1      jsonb             NOT NULL DEFAULT '{}',
    data_2      jsonb             NULL,

    fnr_1       CHAR(11)          NULL,
    aktor_id_1  CHAR(13)          NULL,
    navn_1      gyldig_personnavn NULL     DEFAULT ('Fornavn', NULL, 'Etternavn'),

    boolean_1   BOOLEAN           NOT NULL DEFAULT FALSE,
    boolean_2   BOOLEAN           NULL,

    date_1      DATE              NULL     DEFAULT NOW(),
    time_1      timetz            NULL     DEFAULT NOW(),
    time_2      TIME              NULL     DEFAULT NOW(),
    timestamp_1 timestamptz       NULL     DEFAULT NOW(),
    timestamp_2 TIMESTAMP         NULL     DEFAULT NOW(),

    array_1     TEXT[]            NULL     DEFAULT ARRAY ['a', 'b', 'c']::TEXT[],
    array_2     INTEGER[]         NULL     DEFAULT ARRAY [1, 2, 3]::INTEGER[],

    uuid_1      uuid              NULL     DEFAULT gen_random_uuid(),

    PRIMARY KEY (id)
);
