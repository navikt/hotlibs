package no.nav.hjelpemidler.saksbehandling.core.oppgave

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgaveprioritet(
    override val beskrivelse: String,
    override val eksternKode: String,
) : Kodeverk<Oppgaveprioritet>, EksterntKodeverk {
    KRITISK(beskrivelse = "Kritisk", eksternKode = "KRITISK"),

    @JsonAlias("HOY")
    HØY(beskrivelse = "Høy", eksternKode = "HOY"),

    @JsonAlias("NORM")
    NORMAL(beskrivelse = "Normal", eksternKode = "NORM"),

    LAV(beskrivelse = "Lav", eksternKode = "LAV"),
}
