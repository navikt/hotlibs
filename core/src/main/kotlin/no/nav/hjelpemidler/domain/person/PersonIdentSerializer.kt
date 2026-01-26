package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.IdSerializer

object PersonIdentSerializer : IdSerializer<PersonIdent>(
    serialName = "no.nav.hjelpemidler.domain.person.PersonIdentSerializer",
    creator = ::personIdentOf,
)
