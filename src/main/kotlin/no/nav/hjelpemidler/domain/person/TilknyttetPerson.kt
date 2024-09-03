package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonAlias

interface TilknyttetPerson {
    @get:JsonAlias("brukersFodselsnummer") // fixme -> trenger vi denne?
    val fnr: Fødselsnummer
}
