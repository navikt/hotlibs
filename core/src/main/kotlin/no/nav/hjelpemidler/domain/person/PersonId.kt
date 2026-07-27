package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.id.StringId
import tools.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PersonIdDeserializer::class)
sealed class PersonId(value: String) : StringId(value)

fun personIdOf(value: String): PersonId =
    personIdOrNullOf(value) ?: throw IllegalArgumentException("Ugyldig PersonId")

fun personIdOrNullOf(value: String): PersonId? = when {
    AktørId.erGyldig(value) -> AktørId(value)
    Fødselsnummer.erGyldig(value) -> Fødselsnummer(value)
    else -> null
}
