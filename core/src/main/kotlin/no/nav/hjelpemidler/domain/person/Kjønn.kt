package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class Kjønn {
    MANN,
    KVINNE,

    @JsonEnumDefaultValue
    UKJENT,
}
