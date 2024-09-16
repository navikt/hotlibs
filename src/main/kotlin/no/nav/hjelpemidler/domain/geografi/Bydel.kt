package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Bydel(
    @JsonAlias("bydelsnummer")
    override val nummer: String,
    @JsonAlias("bydelsnavn")
    override val navn: String,
) : GeografiskOmråde()
