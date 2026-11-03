package no.nav.hjelpemidler.oppgave.kodeverk

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgavestatus(
    override val beskrivelse: String,
    val statuskategori: Statuskategori,
) : Kodeverk<Oppgavestatus>, EksterntKodeverk {
    OPPRETTET(
        beskrivelse = "Opprettet",
        statuskategori = Statuskategori.ÅPEN,
    ),

    @EksternKode("AAPNET")
    ÅPNET(
        beskrivelse = "Åpnet",
        statuskategori = Statuskategori.ÅPEN,
    ),

    UNDER_BEHANDLING(
        beskrivelse = "Under behandling",
        statuskategori = Statuskategori.ÅPEN,
    ),

    FERDIGSTILT(
        beskrivelse = "Ferdigstilt",
        statuskategori = Statuskategori.AVSLUTTET,
    ),

    FEILREGISTRERT(
        beskrivelse = "Feilregistrert",
        statuskategori = Statuskategori.AVSLUTTET,
    ),
    ;

    val isFerdigstilt: Boolean get() = this == FERDIGSTILT
    val isFeilregistrert: Boolean get() = this == FEILREGISTRERT

    val isÅpen: Boolean get() = statuskategori == Statuskategori.ÅPEN
    val isAvsluttet: Boolean get() = statuskategori == Statuskategori.AVSLUTTET

    enum class Statuskategori : EksterntKodeverk {
        @EksternKode("AAPEN")
        ÅPEN,
        AVSLUTTET,
    }
}
