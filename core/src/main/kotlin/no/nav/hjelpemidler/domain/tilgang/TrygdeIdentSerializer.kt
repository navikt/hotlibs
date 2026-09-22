package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.domain.id.IdSerializer

object TrygdeIdentSerializer : IdSerializer<TrygdeIdent>(
    serialName = "no.nav.hjelpemidler.domain.tilgang.TrygdeIdentSerializer",
    creator = ::TrygdeIdent,
)
