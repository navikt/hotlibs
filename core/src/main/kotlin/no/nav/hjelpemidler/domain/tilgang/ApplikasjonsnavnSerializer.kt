package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.IdSerializer

object ApplikasjonsnavnSerializer : IdSerializer<Applikasjonsnavn>(
    serialName = "no.nav.hjelpemidler.domain.tilgang.ApplikasjonsnavnSerializer",
    creator = ::Applikasjonsnavn,
)
