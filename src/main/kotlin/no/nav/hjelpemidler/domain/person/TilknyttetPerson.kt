package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonAlias

/**
 * Implementeres av klasser som er tilknyttet en enkelt person. Kan bla. brukes til å implementere
 * tilgangskontroll på tvers av klasser som er knyttet til personer som det skal begrenses tilgang til.
 */
interface TilknyttetPerson {
    @get:JsonAlias("brukersFodselsnummer") // fixme -> trenger vi denne?
    val fnr: Fødselsnummer
}
