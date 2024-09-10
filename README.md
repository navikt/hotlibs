# hm-core

Fellesbibliotek for DigiHoT.

## Pakker

### `no.nav.hjelpemidler.collections`

Inneholder nyttige extensions og funksjoner for collections.

### `no.nav.hjelpemidler.configuration`

Inneholder det som trengs for å gjøre en app konfigurerbar og tilpasset miljøet app-en kjører i.
Pakken inneholder også en rekke objekter med felter for typiske miljøvariabler vi ofte bruker og som defineres
av NAIS.

Sentrale klasser:

* [no.nav.hjelpemidler.configuration.Configuration](/src/main/kotlin/no/nav/hjelpemidler/configuration/Configuration.kt) ->
  Den gjeldende miljøkonfigurasjonen
* [no.nav.hjelpemidler.configuration.Environment](/src/main/kotlin/no/nav/hjelpemidler/configuration/Environment.kt) ->
  Det gjeldende miljøet (e.g. "test", "dev-gcp", "prod-gcp")

Bruk av [EnvironmentVariable](/src/main/kotlin/no/nav/hjelpemidler/configuration/EnvironmentVariable.kt):

```kotlin
object AppConfig {
    val HOST by EnvironmentVariable // påkrevd String
    val PORT: Int by environmentVariable() // påkrevd Int
    val TOKEN: String? by environmentVariable() // valgfri String
}
```

### `no.nav.hjelpemidler.domain.*`

Inneholder domenerelaterte funksjoner og klasser som vi tenker er helt generelle for alle løsningene våre som f.eks.
[Fødseslnummer](/src/main/kotlin/no/nav/hjelpemidler/domain/person/Fødselsnummer.kt).

### `no.nav.hjelpemidler.localization`

Inneholder funksjoner, klasser og konstanter for lokalisering av tekst, tid, beløp, tall osv.

### `no.nav.hjelpemidler.logging`

Inneholder bla. konstanten `secureLog` for logging til sikkerlogg ("tjenestekall").

### `no.nav.hjelpemidler.serialization`

Støttefunksjonalitet for serialisering til og fra f.eks. JSON.

### `no.nav.hjelpemidler.time`

Støttefunksjonalitet og extensions fortrinnsvis tilhørende `java.time.*`, bla. støtte for å legge til arbeidsdager
på en gitt `java.time.Instant`.

## Publisering

Publisering av hm-core gjøres manuelt pt.

Gå til "Releases" -> "Draft a new release" og skriv inn versjonen (e.g. "0.5.0") som skal bygges under "Choose a tag" og
trykk "Enter". Tag-en blir da opprettet og workflow for publisering kjører.

Lag ny release [her](/releases/new).
