package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.IdSerializer

object AktørIdSerializer : IdSerializer<AktørId>(
    serialName = "no.nav.hjelpemidler.domain.person.AktørIdSerializer",
    creator = ::AktørId,
)
