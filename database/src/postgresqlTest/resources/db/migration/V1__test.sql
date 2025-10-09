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
    string      TEXT              NOT NULL,
    integer     INTEGER           NOT NULL,
    long        BIGINT            NOT NULL DEFAULT 0,
    enum        TEXT              NOT NULL,

    data_1      jsonb             NOT NULL,
    data_2      jsonb             NULL,

    fnr         CHAR(11)          NULL,
    aktor_id    CHAR(13)          NULL,
    navn        gyldig_personnavn NULL,

    boolean_1   BOOLEAN           NOT NULL DEFAULT FALSE,
    boolean_2   BOOLEAN           NULL,

    date_1      DATE              NOT NULL DEFAULT NOW(),
    date_2      DATE              NULL,

    time_1      timetz            NOT NULL DEFAULT NOW(),
    time_2      TIME              NULL,

    timestamp_1 timestamptz       NOT NULL DEFAULT NOW(),
    timestamp_2 TIMESTAMP         NULL,

    array_1     TEXT[]            NOT NULL DEFAULT ARRAY ['a', 'b', 'c']::TEXT[],
    array_2     INTEGER[]         NULL,

    PRIMARY KEY (id)
);
