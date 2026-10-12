package no.nav.hjelpemidler.saksbehandling.core.oppgave

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgavestatus(
    override val beskrivelse: String,
    override val eksternKode: String,
    val statuskategori: Statuskategori,
) : Kodeverk<Oppgavestatus>, EksterntKodeverk {
    OPPRETTET(
        beskrivelse = "Opprettet",
        eksternKode = "OPPRETTET",
        statuskategori = Statuskategori.ÅPEN,
    ),

    @JsonAlias("AAPNET")
    ÅPNET(
        beskrivelse = "Åpnet",
        eksternKode = "AAPNET",
        statuskategori = Statuskategori.ÅPEN,
    ),

    UNDER_BEHANDLING(
        beskrivelse = "Under behandling",
        eksternKode = "UNDER_BEHANDLING",
        statuskategori = Statuskategori.ÅPEN,
    ),

    FERDIGSTILT(
        beskrivelse = "Ferdigstilt",
        eksternKode = "FERDIGSTILT",
        statuskategori = Statuskategori.AVSLUTTET,
    ),

    FEILREGISTRERT(
        beskrivelse = "Feilregistrert",
        eksternKode = "FEILREGISTRERT",
        statuskategori = Statuskategori.AVSLUTTET,
    ),
    ;

    val isFerdigstilt: Boolean get() = this == FERDIGSTILT
    val isFeilregistrert: Boolean get() = this == FEILREGISTRERT

    val isÅpen: Boolean get() = statuskategori == Statuskategori.ÅPEN
    val isAvsluttet: Boolean get() = statuskategori == Statuskategori.AVSLUTTET

    enum class Statuskategori(override val eksternKode: String) : EksterntKodeverk {
        ÅPEN(eksternKode = "AAPEN"),
        AVSLUTTET(eksternKode = "AVSLUTTET"),
    }
}
