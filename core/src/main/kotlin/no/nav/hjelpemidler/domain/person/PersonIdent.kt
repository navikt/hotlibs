package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.hjelpemidler.domain.id.StringId

@JsonDeserialize(using = PersonIdentDeserializer::class)
sealed class PersonIdent(value: String) : StringId(value)

fun String.toPersonIdent(): PersonIdent? = when {
    AktørId.erGyldig(this) -> toAktørId()
    Fødselsnummer.erGyldig(this) -> toFødselsnummer()
    else -> null
}

internal fun String.toPersonIdentOrThrow() = toPersonIdent() ?: throw IllegalArgumentException("Ugyldig PersonIdent")
