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
    id                      BIGSERIAL         NOT NULL,

    integer                 INTEGER           NOT NULL DEFAULT 10,
    long                    BIGINT            NOT NULL DEFAULT 20,
    string                  TEXT              NOT NULL DEFAULT 'test',
    enum                    TEXT              NOT NULL DEFAULT 'A',

    data_required           jsonb             NOT NULL DEFAULT '{}',
    data_optional           jsonb             NULL,

    fnr                     CHAR(11)          NULL,
    aktor_id                CHAR(13)          NULL,
    navn                    gyldig_personnavn NULL     DEFAULT ('Fornavn', NULL, 'Etternavn'),

    boolean_required        BOOLEAN           NOT NULL DEFAULT FALSE,
    boolean_optional        BOOLEAN           NULL,

    date                    DATE              NULL     DEFAULT NOW(),
    time                    TIME              NULL     DEFAULT NOW(),
    time_with_timezone      timetz            NULL     DEFAULT NOW(),
    timestamp               TIMESTAMP         NULL     DEFAULT NOW(),
    timestamp_with_timezone timestamptz       NULL     DEFAULT NOW(),

    array_string            TEXT[]            NULL     DEFAULT ARRAY ['a', 'b', 'c']::TEXT[],
    array_integer           INTEGER[]         NULL     DEFAULT ARRAY [1, 2, 3]::INTEGER[],

    uuid                    uuid              NULL     DEFAULT gen_random_uuid(),

    PRIMARY KEY ("id")
);
