package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Enhet(
    @JsonAlias("enhetsnummer")
    override val nummer: String,
    @JsonAlias("enhetsnavn")
    override val navn: String,
) : GeografiskOmråde()
