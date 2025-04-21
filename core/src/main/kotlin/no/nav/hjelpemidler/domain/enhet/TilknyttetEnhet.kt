package no.nav.hjelpemidler.domain.enhet

import com.fasterxml.jackson.annotation.JsonIgnore

interface TilknyttetEnhet {
    val enhet: Enhet
    val enhetsnummer: String
        @JsonIgnore
        get() = enhet.nummer
}
