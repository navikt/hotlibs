package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.IdSerializer

object FødselsnummerSerializer : IdSerializer<Fødselsnummer>(
    serialName = "no.nav.hjelpemidler.domain.person.FødselsnummerSerializer",
    creator = ::Fødselsnummer,
)
