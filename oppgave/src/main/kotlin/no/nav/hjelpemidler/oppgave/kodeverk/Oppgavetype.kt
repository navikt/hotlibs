package no.nav.hjelpemidler.saksbehandling.core.oppgave

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Oppgavetype(
    override val beskrivelse: String,
    override val eksternKode: String,
) : Kodeverk<Oppgavetype>, EksterntKodeverk {
    @JsonAlias("RETUR")
    BEHANDLE_RETURPOST(beskrivelse = "Behandle returpost", eksternKode = "RETUR"),

    @JsonAlias("BEH_SAK")
    BEHANDLE_SAK(beskrivelse = "Behandle sak", eksternKode = "BEH_SAK"),

    @JsonAlias("BEH_SAK_MK")
    BEHANDLE_SAK_MANUELT(beskrivelse = "Behandle sak (Manuell)", eksternKode = "BEH_SAK_MK"),

    @JsonAlias("BEH_SED")
    BEHANDLE_SED(beskrivelse = "Behandle SED", eksternKode = "BEH_SED"),

    @JsonAlias("BEH_UND_VED")
    BEHANDLE_UNDERKJENT_VEDTAK(beskrivelse = "Behandle underkjent vedtak", eksternKode = "BEH_UND_VED"),

    @JsonAlias("FLY")
    FLYTTESAK(beskrivelse = "Flyttesak", eksternKode = "FLY"),

    @JsonAlias("FDR")
    FORDELING(beskrivelse = "Fordeling", eksternKode = "FDR"),

    @JsonAlias("GOD_VED")
    GODKJENNE_VEDTAK(beskrivelse = "Godkjenne vedtak", eksternKode = "GOD_VED"),

    @JsonAlias("HJELP_UTPROV")
    HJELP_TIL_UTPRØVING(beskrivelse = "Hjelp til utprøving", eksternKode = "HJELP_UTPROV"),

    @JsonAlias("INNH_DOK")
    INNHENT_DOKUMENTASJON(beskrivelse = "Innhent dokumentasjon", eksternKode = "INNH_DOK"),

    @JsonAlias("JFR")
    JOURNALFØRING(beskrivelse = "Journalføring", eksternKode = "JFR"),

    @JsonAlias("KONT_BRUK")
    KONTAKT_BRUKER(beskrivelse = "Kontakt bruker", eksternKode = "KONT_BRUK"),

    @JsonAlias("KON_UTG_SCA_DOK")
    KONTROLLER_UTGÅENDE_SKANNET_DOKUMENT(
        beskrivelse = "Kontroller utgående skannet dokument",
        eksternKode = "KON_UTG_SCA_DOK",
    ),

    @JsonAlias("MOTK")
    KRAV_MOTTATT(beskrivelse = "Krav mottatt", eksternKode = "MOTK"),

    @JsonAlias("ROB_BEH")
    ROBOTBEHANDLING(beskrivelse = "Robotbehandling", eksternKode = "ROB_BEH"),

    @JsonAlias("SVAR_IK_MOT")
    SVAR_IKKE_MOTTATT(beskrivelse = "Svar ikke mottatt", eksternKode = "SVAR_IK_MOT"),

    @JsonAlias("VURD_BREV")
    VURDER_BREV(beskrivelse = "Vurder brev", eksternKode = "VURD_BREV"),

    @JsonAlias("VUR")
    VURDER_DOKUMENT(beskrivelse = "Vurder dokument", eksternKode = "VUR"),

    @JsonAlias("VURD_HENV")
    VURDER_HENVENDELSE(beskrivelse = "Vurder henvendelse", eksternKode = "VURD_HENV"),

    @JsonAlias("VUR_KONS_YTE")
    VURDER_KONSEKVENS_YTELSE(beskrivelse = "Vurder konsekvens for ytelse", eksternKode = "VUR_KONS_YTE"),

    @JsonAlias("VURD_NOTAT")
    VURDER_NOTAT(beskrivelse = "Vurder notat", eksternKode = "VURD_NOTAT"),

    @JsonAlias("VUR_SVAR")
    VURDER_SVAR(beskrivelse = "Vurder svar", eksternKode = "VUR_SVAR"),
}
