# hotlibs

Samling av fellesbiblioteker for DigiHoT.

## Biblioteker

* [Core](/core/README.md): Konfigurasjon og sentrale domeneklasser som Fødselsnummer etc.
* [Database](/database/README.md): Databasekommunikasjon og databasetesting.
* [HTTP](/http/README.md): HTTP-kommunikasjon og OpenID.
* [Katalog](/katalog/README.md): Gradle Version Catalog for DigiHoT.
* [NARE](/nare/README.md): Not a rule engine, for regelspesifisering.

## Publisering

Nye versjoner av alle biblioteker publiseres ved push til main.

## Oracle

```bash
docker run --name oracle-free -p 1521:1521 -e ORACLE_PWD=system -d container-registry.oracle.com/database/free:latest-lite
```
