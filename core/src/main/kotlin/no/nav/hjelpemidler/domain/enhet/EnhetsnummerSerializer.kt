package no.nav.hjelpemidler.domain.enhet

import no.nav.hjelpemidler.domain.id.IdSerializer

object EnhetsnummerSerializer : IdSerializer<Enhetsnummer>(
    serialName = "no.nav.hjelpemidler.domain.enhet.EnhetsnummerSerializer",
    creator = ::Enhetsnummer,
)
