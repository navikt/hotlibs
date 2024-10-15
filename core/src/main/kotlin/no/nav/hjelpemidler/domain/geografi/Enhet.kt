package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Enhet(
    @JsonAlias("enhetsnummer", "enhetNr")
    override val nummer: String,
    @JsonAlias("enhetsnavn")
    override val navn: String,
) : GeografiskOmråde() {
    // for extensions
    companion object
}
