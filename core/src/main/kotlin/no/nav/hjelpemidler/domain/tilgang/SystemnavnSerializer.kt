package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.IdSerializer

object SystemnavnSerializer : IdSerializer<Systemnavn>(
    serialName = "no.nav.hjelpemidler.domain.tilgang.SystemnavnSerializer",
    creator = ::Systemnavn,
)
