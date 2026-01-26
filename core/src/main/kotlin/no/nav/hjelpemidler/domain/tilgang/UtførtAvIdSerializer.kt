package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.IdSerializer

object UtførtAvIdSerializer : IdSerializer<UtførtAvId>(
    serialName = "no.nav.hjelpemidler.domain.tilgang.UtførtAvIdSerializer",
    creator = ::utførtAvIdOf,
)
