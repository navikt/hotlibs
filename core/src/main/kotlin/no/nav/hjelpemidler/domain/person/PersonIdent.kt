package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.StringId

sealed class PersonIdent(value: String) : StringId(value)
