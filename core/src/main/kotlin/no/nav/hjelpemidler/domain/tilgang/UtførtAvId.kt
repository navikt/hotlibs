package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.id.StringId

sealed class UtførtAvId(value: String) : StringId(value) {
    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): UtførtAvId = when {
            NavIdent.erGyldig(value) -> NavIdent(value)
            TrygdeIdent.erGyldig(value) -> TrygdeIdent(value)
            else -> Applikasjonsnavn(value)
        }
    }

    data object Ukjent : UtførtAvId("Ukjent")
}
