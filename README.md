# hotlibs

Samling av fellesbiblioteker for DigiHoT.

## Biblioteker

* [Behovsmelding](/behovsmelding): Felles modell for behovsmelding.
* [Core](/core/README.md): Konfigurasjon og sentrale domeneklasser som Fødselsnummer etc.
* [Database](/database/README.md): Databasekommunikasjon og databasetesting.
* [HTTP](/http/README.md): HTTP-kommunikasjon og OpenID.
* [Kafka](/kafka): Kafka-utvidelser.
* [Katalog](/katalog/README.md): Gradle Version Catalog for DigiHoT.
* [Logging](/logging): Logging-utvidelser og felles Logback-konfigurasjon.
* [NARE](/nare/README.md): Not a rule engine, for regelspesifisering.
* [Oppgave](/oppgave): Felles modell for oppgave.
* [Platform](/platform): Felles Gradle platform for DigiHoT.
* [Rapids and rivers](/rapids-and-rivers): Utvidelser til Rapids and rivers.
* [Serialization](/serialization): Jackson-utvidelser og felles Jackson-konfigurasjon.
* [Streams](/streams): Ktor-støtte for Kafka Streams.
* [Test](/test): Felles test-bibliotek.

## Publisering

Nye versjoner av alle biblioteker publiseres ved push til main.

## Migrering fra Jackson 2 til 3

1. Endre alle importer fra `com.fasterxml.jackson.*` til `tools.jackson.*`, bortsett fra
   `com.fasterxml.jackson.annotation.*`.
2. Fjern bruk av `JavaTimeModule`, den er nå med som default siden Jackson 3.0 krever JDK >= 17.
3. TODO

## Oracle

```bash
docker run --name oracle-free -p 1521:1521 -e ORACLE_PWD=system -d container-registry.oracle.com/database/free:latest-lite
```
