package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.IdSerializer

object NavIdentSerializer : IdSerializer<NavIdent>(
    serialName = "no.nav.hjelpemidler.domain.tilgang.NavIdentSerializer",
    creator = ::NavIdent,
)
