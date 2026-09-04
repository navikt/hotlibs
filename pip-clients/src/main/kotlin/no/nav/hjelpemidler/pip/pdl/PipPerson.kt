package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.pip.pdl.PipAdressebeskyttelse

data class PipPerson(
    @JsonProperty("adressebeskyttelse") val adressebeskyttelse: List<PipAdressebeskyttelse>,
)
