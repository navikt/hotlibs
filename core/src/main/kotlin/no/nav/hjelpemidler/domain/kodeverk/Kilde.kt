package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JsonAlias

enum class Kilde {
    DIGITAL_BEHOVSMELDING,

    @JsonAlias("PAPIR")
    JOURNALPOST,
}
