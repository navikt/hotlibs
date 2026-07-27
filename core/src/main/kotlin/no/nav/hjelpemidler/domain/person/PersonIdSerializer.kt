package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.IdSerializer

object PersonIdSerializer : IdSerializer<PersonId>(
    serialName = "no.nav.hjelpemidler.domain.person.PersonIdSerializer",
    creator = ::personIdOf,
)
