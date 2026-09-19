package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.id.BrukerId
import no.nav.hjelpemidler.validation.Validator

sealed class PersonId(value: String) : BrukerId(value) {
    companion object : Validator<String> {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): PersonId = when {
            AktørId.erGyldig(value) -> AktørId(value)
            Fødselsnummer.erGyldig(value) -> Fødselsnummer(value)
            else -> throw IllegalArgumentException("Ugyldig PersonId")
        }

        override fun erGyldig(value: String): Boolean = AktørId.erGyldig(value) || Fødselsnummer.erGyldig(value)
    }
}
