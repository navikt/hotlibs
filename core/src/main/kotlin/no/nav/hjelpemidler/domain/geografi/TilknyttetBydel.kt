package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonIgnore

interface TilknyttetBydel {
    val bydel: Bydel?
    val bydelsnummer: String?
        @JsonIgnore
        get() = bydel?.nummer
}
