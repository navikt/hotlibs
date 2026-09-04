package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonProperty

data class PipIdenter(
    @JsonProperty("identer") val identer: List<PipIdent>,
) : Iterable<PipIdent> by identer
