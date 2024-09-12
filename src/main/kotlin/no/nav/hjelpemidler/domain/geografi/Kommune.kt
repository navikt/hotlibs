package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Kommune(
    @JsonAlias("kommunenummer")
    override val nummer: String,
    @JsonAlias("kommunenavn")
    override val navn: String,
) : GeografiskOmråde()
