CREATE TYPE personnavn AS
(
    fornavn    TEXT,
    mellomnavn TEXT,
    etternavn  TEXT
);

CREATE DOMAIN gyldig_personnavn AS personnavn
    CHECK (value IS NULL OR ((value).fornavn IS NOT NULL AND (value).etternavn IS NOT NULL));

CREATE DOMAIN fodselsnummer AS CHAR(11);
CREATE DOMAIN aktor_id AS CHAR(13);

CREATE TABLE test
(
    id                      BIGSERIAL PRIMARY KEY,

    boolean                 BOOLEAN,
    enum                    TEXT              DEFAULT 'A',
    integer                 INTEGER           DEFAULT 10,
    long                    BIGINT            DEFAULT 20,
    string                  TEXT              DEFAULT 'test',
    uuid                    uuid              DEFAULT gen_random_uuid(),

    date                    DATE              DEFAULT NOW(),
    time                    TIME              DEFAULT NOW(),
    time_with_timezone      timetz            DEFAULT NOW(),
    timestamp               TIMESTAMP         DEFAULT NOW(),
    timestamp_with_timezone timestamptz       DEFAULT NOW(),

    fnr                     fodselsnummer,
    aktor_id                aktor_id,
    navn                    gyldig_personnavn DEFAULT ('Grønn Rolig', NULL, 'Bolle'),

    strings                 TEXT[]            DEFAULT ARRAY ['a', 'b', 'c']::TEXT[],
    integers                INTEGER[]         DEFAULT ARRAY [1, 2, 3]::INTEGER[],

    data                    jsonb             DEFAULT '{}'
);
