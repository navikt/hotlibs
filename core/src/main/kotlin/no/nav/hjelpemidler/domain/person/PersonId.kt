package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.id.StringId

sealed class PersonId(value: String) : StringId(value) {
    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun from(value: String): PersonId = when {
            AktørId.erGyldig(value) -> AktørId(value)
            Fødselsnummer.erGyldig(value) -> Fødselsnummer(value)
            else -> throw IllegalArgumentException("Ugyldig PersonId")
        }
    }
}
