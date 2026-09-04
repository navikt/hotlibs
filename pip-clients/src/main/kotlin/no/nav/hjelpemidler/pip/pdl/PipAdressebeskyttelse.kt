package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering

data class PipAdressebeskyttelse(@JsonProperty("gradering") val gradering: AdressebeskyttelseGradering?)
