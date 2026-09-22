package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.annotation.JsonCreator

sealed interface AnsattId {
    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(value: String): AnsattId = when {
            NavIdent.erGyldig(value) -> NavIdent(value)
            TrygdeIdent.erGyldig(value) -> TrygdeIdent(value)
            else -> throw IllegalArgumentException("Ukjent ansattId: '$value'")
        }
    }
}
