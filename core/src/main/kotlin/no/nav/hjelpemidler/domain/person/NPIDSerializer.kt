package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.IdSerializer

object NPIDSerializer : IdSerializer<NPID>(
    serialName = "no.nav.hjelpemidler.domain.person.NPIDSerializer",
    creator = ::NPID,
)
