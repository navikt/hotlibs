CREATE DOMAIN fodselsnummer AS CHAR(11);

CREATE TABLE sak_v1
(
    id           BIGSERIAL PRIMARY KEY,
    sakstype     TEXT          NOT NULL,
    fnr          fodselsnummer NOT NULL,
    saksgrunnlag jsonb         NOT NULL DEFAULT '{}',
    opprettet    timestamptz   NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc')
);

CREATE TABLE saksstatus_v1
(
    id         BIGSERIAL PRIMARY KEY,
    sak_id     BIGINT NOT NULL REFERENCES sak_v1,
    saksstatus TEXT   NOT NULL,
    gyldig_fra timestamptz DEFAULT (NOW() AT TIME ZONE 'utc')
);
