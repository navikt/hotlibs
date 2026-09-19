package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.organisasjon.Organisasjonsnummer
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

abstract class BrukerId(value: String) : StringId(value) {
    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): BrukerId = when {
            AktørId.erGyldig(value) -> AktørId(value)
            Fødselsnummer.erGyldig(value) -> Fødselsnummer(value)
            Organisasjonsnummer.erGyldig(value) -> Organisasjonsnummer(value)
            else -> throw IllegalArgumentException("Ugyldig BrukerId")
        }
    }
}
