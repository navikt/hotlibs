package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.id.StringId

sealed class UtførtAvId(value: String) : StringId(value) {
    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun from(value: String): UtførtAvId = if (NavIdent.erGyldig(value)) NavIdent(value) else Applikasjonsnavn(value)
    }
}
