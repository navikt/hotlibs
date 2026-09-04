package no.nav.hjelpemidler.pip.skjerming

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.Fødselsnummer

data class ErSkjermedePersonerRequest(@JsonProperty("personidenter") val fnr: Collection<Fødselsnummer>)
