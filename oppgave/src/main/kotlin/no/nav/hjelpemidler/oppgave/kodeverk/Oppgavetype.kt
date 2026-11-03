package no.nav.hjelpemidler.oppgave.kodeverk

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgavetype(override val beskrivelse: String) : Kodeverk<Oppgavetype>, EksterntKodeverk {
    @EksternKode("RETUR")
    BEHANDLE_RETURPOST(beskrivelse = "Behandle returpost"),

    @EksternKode("BEH_SAK")
    BEHANDLE_SAK(beskrivelse = "Behandle sak"),

    @EksternKode("BEH_SAK_MK")
    BEHANDLE_SAK_MANUELT(beskrivelse = "Behandle sak (Manuell)"),

    @EksternKode("BEH_SED")
    BEHANDLE_SED(beskrivelse = "Behandle SED"),

    @EksternKode("BEH_UND_VED")
    BEHANDLE_UNDERKJENT_VEDTAK(beskrivelse = "Behandle underkjent vedtak"),

    @EksternKode("FLY")
    FLYTTESAK(beskrivelse = "Flyttesak"),

    @EksternKode("FDR")
    FORDELING(beskrivelse = "Fordeling"),

    @EksternKode("GOD_VED")
    GODKJENNE_VEDTAK(beskrivelse = "Godkjenne vedtak"),

    @EksternKode("HJELP_UTPROV")
    HJELP_TIL_UTPRØVING(beskrivelse = "Hjelp til utprøving"),

    @EksternKode("INNH_DOK")
    INNHENT_DOKUMENTASJON(beskrivelse = "Innhent dokumentasjon"),

    @EksternKode("JFR")
    JOURNALFØRING(beskrivelse = "Journalføring"),

    @EksternKode("KONT_BRUK")
    KONTAKT_BRUKER(beskrivelse = "Kontakt bruker"),

    @EksternKode("KON_UTG_SCA_DOK")
    KONTROLLER_UTGÅENDE_SKANNET_DOKUMENT(beskrivelse = "Kontroller utgående skannet dokument"),

    @EksternKode("MOTK")
    KRAV_MOTTATT(beskrivelse = "Krav mottatt"),

    @EksternKode("ROB_BEH")
    ROBOTBEHANDLING(beskrivelse = "Robotbehandling"),

    @EksternKode("SVAR_IK_MOT")
    SVAR_IKKE_MOTTATT(beskrivelse = "Svar ikke mottatt"),

    @EksternKode("VURD_BREV")
    VURDER_BREV(beskrivelse = "Vurder brev"),

    @EksternKode("VUR")
    VURDER_DOKUMENT(beskrivelse = "Vurder dokument"),

    @EksternKode("VURD_HENV")
    VURDER_HENVENDELSE(beskrivelse = "Vurder henvendelse"),

    @EksternKode("VUR_KONS_YTE")
    VURDER_KONSEKVENS_YTELSE(beskrivelse = "Vurder konsekvens for ytelse"),

    @EksternKode("VURD_NOTAT")
    VURDER_NOTAT(beskrivelse = "Vurder notat"),

    @EksternKode("VUR_SVAR")
    VURDER_SVAR(beskrivelse = "Vurder svar"),
}
