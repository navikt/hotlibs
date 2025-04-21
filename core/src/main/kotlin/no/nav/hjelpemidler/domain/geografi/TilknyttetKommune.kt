package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonIgnore

interface TilknyttetKommune {
    val kommune: Kommune?
    val kommunenummer: String?
        @JsonIgnore
        get() = kommune?.nummer
}
