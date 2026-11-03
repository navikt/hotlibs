package no.nav.hjelpemidler.oppgave.kodeverk

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgaveprioritet(override val beskrivelse: String) : Kodeverk<Oppgaveprioritet>, EksterntKodeverk {
    KRITISK(beskrivelse = "Kritisk"),

    @EksternKode("HOY")
    HØY(beskrivelse = "Høy"),

    @EksternKode("NORM")
    NORMAL(beskrivelse = "Normal"),

    LAV(beskrivelse = "Lav"),
}
