package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.nav.hjelpemidler.domain.id.StringId

@JsonDeserialize(using = PersonIdentDeserializer::class)
sealed class PersonIdent(value: String) : StringId(value)

fun personIdentOf(value: String): PersonIdent =
    personIdentOrNullOf(value) ?: throw IllegalArgumentException("Ugyldig PersonIdent")

fun personIdentOrNullOf(value: String): PersonIdent? = when {
    AktørId.erGyldig(value) -> AktørId(value)
    Fødselsnummer.erGyldig(value) -> Fødselsnummer(value)
    else -> null
}
