package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.StringId

sealed class PersonIdent(value: String) : StringId(value)

fun String.toPersonIdent(): PersonIdent? = when {
    AktørId.erGyldig(this) -> toAktørId()
    Fødselsnummer.erGyldig(this) -> toFødselsnummer()
    else -> null
}
