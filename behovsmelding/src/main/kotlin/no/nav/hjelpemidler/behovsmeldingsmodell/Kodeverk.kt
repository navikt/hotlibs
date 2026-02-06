package no.nav.hjelpemidler.behovsmeldingsmodell

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.behovsmeldingsmodell.sak.Vedtaksresultat
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class BehovsmeldingType {
    SØKNAD,
    BESTILLING,
    BYTTE,
    BRUKERPASSBYTTE,
}

enum class BehovsmeldingStatus {
    VENTER_GODKJENNING,
    GODKJENT_MED_FULLMAKT,
    INNSENDT_FULLMAKT_IKKE_PÅKREVD,
    BRUKERPASSBYTTE_INNSENDT,
    GODKJENT,
    SLETTET,
    UTLØPT,
    ENDELIG_JOURNALFØRT,
    BESTILLING_FERDIGSTILT,
    BESTILLING_AVVIST,
    VEDTAKSRESULTAT_INNVILGET,
    VEDTAKSRESULTAT_MUNTLIG_INNVILGET,
    VEDTAKSRESULTAT_DELVIS_INNVILGET,
    VEDTAKSRESULTAT_AVSLÅTT,
    VEDTAKSRESULTAT_ANNET,
    UTSENDING_STARTET,
    VEDTAKSRESULTAT_HENLAGTBORTFALT,

    /**
     * Brukes når det endres fra brukerbekreftelse til fullmakt som en midlertidig status inntil
     * ny PDF har blitt generert. Går deretter videre til GODKJENT_MED_FULLMAKT.
     */
    FULLMAKT_AVVENTER_PDF,
    ;

    fun isSlettetEllerUtløpt(): Boolean = this == SLETTET || this == UTLØPT

    companion object {
        fun fraVedtaksresultat(vedtaksresultat: Vedtaksresultat): BehovsmeldingStatus {
            return when (vedtaksresultat.vedtaksresultat) {
                "A", "AVSLÅTT" -> VEDTAKSRESULTAT_AVSLÅTT
                "DI", "DELVIS_INNVILGET" -> VEDTAKSRESULTAT_DELVIS_INNVILGET
                "IM" -> VEDTAKSRESULTAT_MUNTLIG_INNVILGET
                "I", "INNVILGET" -> VEDTAKSRESULTAT_INNVILGET
                "HB" -> VEDTAKSRESULTAT_HENLAGTBORTFALT
                else -> VEDTAKSRESULTAT_ANNET
            }
        }
    }
}

enum class InnsenderRolle {
    FORMIDLER,
    BESTILLER,
}

enum class Signaturtype {
    BRUKER_BEKREFTER,
    FULLMAKT,
    FRITAK_FRA_FULLMAKT,
    IKKE_INNHENTET_FORDI_BYTTE,
    IKKE_INNHENTET_FORDI_BRUKERPASSBYTTE,
    IKKE_INNHENTET_FORDI_KUN_TILBEHØR,
    IKKE_INNHENTET_FORDI_KUN_TILBEHØR_V2,
    IKKE_INNHENTET_FORDI_KUN_TILBEHØR_V3,
}

enum class Brukerkilde {
    PDL,
    FORMIDLER,
}

@Deprecated("Erstattet av bruksarena per hjelpemiddel")
enum class Boform {
    @JsonProperty("Hjemme")
    HJEMME,

    @JsonProperty("Institusjon")
    INSTITUSJON,

    @JsonProperty("Hjemme i egen bolig")
    HJEMME_I_EGEN_BOLIG,

    @JsonProperty("Hjemme i omsorgsbolig, bofellesskap eller servicebolig")
    HJEMME_I_EGEN_BOLIG_OMSORGSBOLIG_BOFELLESSKAP_SERVICEBOLIG,
}

enum class BrukersituasjonVilkår {

    @Deprecated("Ikke lenger et valg i hm-soknad")
    NEDSATT_FUNKSJON,

    @Deprecated("Ikke lenger et valg i hm-soknad")
    @JsonProperty("STORRE_BEHOV")
    STØRRE_BEHOV,

    @Deprecated("Ikke lenger et valg i hm-soknad")
    PRAKTISKE_PROBLEM,

    PRAKTISKE_PROBLEMER_I_DAGLIGLIVET_V1,
    VESENTLIG_OG_VARIG_NEDSATT_FUNKSJONSEVNE_V1,

    @JsonProperty("KAN_IKKE_LOESES_MED_ENKLERE_HJELPEMIDLER_V1")
    KAN_IKKE_LØSES_MED_ENKLERE_HJELPEMIDLER_V1,

    /**
     * NB! Det var en skrivefeil i denne, derfor @JsonProperty.
     */
    @JsonProperty("I_STAND_TIL_AA_BRUKE_HJELEPMIDLENE_V1")
    I_STAND_TIL_Å_BRUKE_HJELPEMIDLENE_V1,
}

enum class BrukersituasjonVilkårtype {

    @Deprecated("Ikke lenger et valg i hm-soknad")
    NEDSATT_FUNKSJON,

    @Deprecated("Ikke lenger et valg i hm-soknad")
    STØRRE_BEHOV,

    @Deprecated("Ikke lenger et valg i hm-soknad")
    PRAKTISKE_PROBLEM,

    PRAKTISKE_PROBLEMER_I_DAGLIGLIVET_V1,
    VESENTLIG_OG_VARIG_NEDSATT_FUNKSJONSEVNE_V1,
    KAN_IKKE_LØSES_MED_ENKLERE_HJELPEMIDLER_V1,
    I_STAND_TIL_Å_BRUKE_HJELPEMIDLENE_V1,
}

enum class Funksjonsnedsettelser {
    BEVEGELSE,
    KOGNISJON,
    HØRSEL,
    SYN,
    KOMMUNIKASJON,
}

enum class LeveringTilleggsinfo {
    UTLEVERING_KALENDERAPP,
    ALLE_HJELPEMIDLER_ER_UTLEVERT,
}

/**
 * Oppfølgings- og opplæringsansvarlig
 */
enum class Oppfølgingsansvarlig {
    @JsonProperty("Hjelpemiddelformidler")
    HJELPEMIDDELFORMIDLER,

    @JsonProperty("NoenAndre")
    ANNEN_OPPFØLGINGSANSVARLIG,
}

enum class OppfølgingsansvarligV2 {
    HJELPEMIDDELFORMIDLER,
    ANNEN_OPPFØLGINGSANSVARLIG,
}

enum class Kontaktperson {
    @JsonProperty("Hjelpemiddelbruker")
    HJELPEMIDDELBRUKER,

    @JsonProperty("Hjelpemiddelformidler")
    HJELPEMIDDELFORMIDLER,

    @JsonProperty("AnnenKontaktperson")
    ANNEN_KONTAKTPERSON,
}

enum class KontaktpersonV2 {
    HJELPEMIDDELBRUKER,
    HJELPEMIDDELFORMIDLER,
    ANNEN_KONTAKTPERSON,
}

enum class Utleveringsmåte {
    @JsonProperty("FolkeregistrertAdresse")
    FOLKEREGISTRERT_ADRESSE,

    @JsonProperty("AnnenBruksadresse")
    ANNEN_BRUKSADRESSE,

    @JsonProperty("Hjelpemiddelsentralen")
    HJELPEMIDDELSENTRALEN,

    @Deprecated("Brukes ikke i digital behovsmelding lenger")
    @JsonProperty("AlleredeUtlevertAvNav")
    ALLEREDE_UTLEVERT_AV_NAV,
}

enum class UtleveringsmåteV2 {
    FOLKEREGISTRERT_ADRESSE,
    ANNEN_BRUKSADRESSE,
    HJELPEMIDDELSENTRALEN,

    @Deprecated("Brukes ikke i digital behovsmelding lenger")
    ALLEREDE_UTLEVERT_AV_NAV,
}

enum class Hasteårsak {
    UTVIKLING_AV_TRYKKSÅR,
    TERMINALPLEIE,

    @Deprecated("Erstattet av _V2")
    UTSKRIVING_FRA_SYKEHUS_SOM_IKKE_KAN_PLANLEGGES,

    @Deprecated("Erstattet av _V3")
    UTSKRIVING_FRA_SYKEHUS_SOM_IKKE_KAN_PLANLEGGES_V2,
    UTSKRIVING_FRA_SYKEHUS_SOM_IKKE_KAN_PLANLEGGES_V3,
    RASK_FORVERRING_AV_ALVORLIG_DIAGNOSE,
    ANNET,
}

enum class Bruksarena {
    EGET_HJEM,
    EGET_HJEM_IKKE_AVLASTNING,
    OMSORGSBOLIG_BOFELLESKAP_SERVICEBOLIG,
    BARNEHAGE,

    @JsonProperty("GRUNN_ELLER_VIDEREGÅENDESKOLE")
    GRUNN_ELLER_VIDEREGÅENDE_SKOLE,
    SKOLEFRITIDSORDNING,
    INSTITUSJON,
    INSTITUSJON_BARNEBOLIG,

    /**
     * NB! Misvisende navn. Skal KUN være til personlig bruk.
     */
    @JsonProperty("INSTITUSJON_BARNEBOLIG_IKKE_PERSONLIG_BRUK")
    INSTITUSJON_BARNEBOLIG_KUN_PERSONLIG_BRUK,
}

enum class BruksarenaV2 {
    EGET_HJEM,
    EGET_HJEM_IKKE_AVLASTNING,
    OMSORGSBOLIG_BOFELLESKAP_SERVICEBOLIG,
    BARNEHAGE,
    GRUNN_ELLER_VIDEREGÅENDE_SKOLE,
    SKOLEFRITIDSORDNING,
    INSTITUSJON,
    INSTITUSJON_BARNEBOLIG,
    INSTITUSJON_BARNEBOLIG_KUN_PERSONLIG_BRUK,
}

enum class KanIkkeAvhjelpesMedEnklereÅrsak {
    HAR_LUFTVEISPROBLEMER,
    BEGRENSNING_VED_FUNKSJONSNEDSETTELSE,
    ANNET,
}

enum class ÅrsakForAntall {
    BEHOV_I_FLERE_ETASJER,
    BEHOV_I_FLERE_ROM,
    BEHOV_INNENDØRS_OG_UTENDØRS,
    BEHOV_FOR_FLERE_PUTER_FOR_RULLESTOL,
    BEHOV_FOR_JEVNLIG_VASK_ELLER_VEDLIKEHOLD,
    BRUKER_HAR_TO_HJEM,
    ANNET_BEHOV,
    PUTENE_SKAL_KOMBINERES_I_POSISJONERING,
    BEHOV_HJEMME_OG_I_BARNEHAGE,
    PUTENE_SKAL_SETTES_SAMMEN_VED_BRUK,
}

enum class BytteÅrsak {
    UTSLITT,
    VOKST_FRA,
    ENDRINGER_I_INNBYGGERS_FUNKSJON,
    FEIL_STØRRELSE,
    VURDERT_SOM_ØDELAGT_AV_LOKAL_TEKNIKER,
}

enum class OppreisningsstolLøftType {
    SKRÅLØFT,
    RETTLØFT,
}

enum class OppreisningsstolBruksområde {
    EGEN_BOENHET,
    FELLESAREAL,
}

enum class OppreisningsstolBehov {
    OPPGAVER_I_DAGLIGLIVET,
    PLEID_I_HJEMMET,
    FLYTTE_MELLOM_STOL_OG_RULLESTOL,
}

enum class SidebetjeningspanelPosisjon {
    HØYRE,
    VENSTRE,
}

enum class PosisjoneringsputeForBarnBruk {
    TILRETTELEGGE_UTGANGSSTILLING,
    TRENING_AKTIVITET_STIMULERING,
}

enum class PosisjoneringsputeBehov {
    STORE_LAMMELSER,
    DIREKTE_AVHJELPE_I_DAGLIGLIVET,
}

enum class PosisjoneringsputeOppgaverIDagligliv {
    SPISE_DRIKKE_OL,
    BRUKE_DATAUTSTYR,
    FØLGE_OPP_BARN,
    HOBBY_FRITID_U26,
    ANNET,
}

enum class BruksområdeGanghjelpemiddel {
    TIL_FORFLYTNING,
    TIL_TRENING_OG_ANNET,
}

enum class GanghjelpemiddelType {
    GÅBORD,
    SPARKESYKKEL,
    KRYKKE,
    GÅTRENING,
    GÅSTOL,
}

enum class PlasseringType {
    @JsonProperty("Venstre")
    VENSTRE,

    @JsonProperty("Høyre")
    HØYRE,
}

enum class UtlevertType {
    @JsonProperty("FremskuttLager")
    FREMSKUTT_LAGER,

    @JsonProperty("Korttidslån")
    KORTTIDSLÅN,

    @JsonProperty("Overført")
    OVERFØRT,

    @JsonProperty("Annet")
    ANNET,
}

enum class UtlevertTypeV2 {
    FREMSKUTT_LAGER,
    KORTTIDSLÅN,
    OVERFØRT,
    ANNET,
}

enum class SitteputeValg {
    @JsonProperty("TrengerSittepute")
    TRENGER_SITTEPUTE,

    @JsonProperty("HarFraFor")
    HAR_FRA_FØR,

    @JsonProperty("StandardSittepute")
    @Deprecated("Ikke lenger et valg. Tidligere brukt for å legge til et tilbehør med navn 'Standard sittepute' og hmsArtNr '000000'")
    STANDARD_SITTEPUTE,

    @JsonProperty("LeggesTilSeparat")
    @Deprecated("Ikke lenger et valg.")
    LEGGES_TIL_SEPARAT,
}

enum class BehovForSeng {
    DYSFUNKSJONELT_SØVNMØNSTER,
    RISIKO_FOR_FALL_UT_AV_SENG,
    STERKE_UFRIVILLIGE_BEVEGELSER,
    ANNET_BEHOV,
}

enum class MadrassValg {
    @JsonProperty("TrengerMadrass")
    TRENGER_MADRASS,

    @JsonProperty("HarFraFor")
    HAR_FRA_FØR,
}

enum class AutomatiskGenerertTilbehør {
    @JsonProperty("Sittepute")
    SITTEPUTE,
}

enum class FritakFraBegrunnelseÅrsak {
    ER_PÅ_BESTILLINGSORDNING,
    IKKE_I_PILOT,
    ER_SELVFORKLARENDE_TILBEHØR,
}

enum class Prioritet {
    NORMAL,
    HAST,
}

enum class OpplysningId : Kodeverk<OpplysningId> {
    ANNEN_INFORMASJON,
    ANTALL_BEGRUNNELSE,
    APP_ADMINISTRERING_AV_STØTTEPERSON,
    APP_UTPRØVING_FOR_BRUKER,
    APP_UTPRØVING_FOR_STØTTEPERSON,
    BRUKSARENA,
    BYTTE_ELLER_EN_TIL,
    DRIVAGGREGAT_HAR_BEHOV_FOR_LEDSAGERSTYRING,
    DRIVAGGREGAT_HAR_LIK_KRAFT_I_BEGGE_ARMER,
    DRIVAGGREGAT_HAR_LIK_KRAFT_I_BEGGE_ARMER_BEGRUNNELSE,
    DRIVAGGREGAT_HVILKEN_RULLESTOL,
    DRIVAGGREGAT_JOYSTICK_PLASSERING,
    DRIVAGGREGAT_KAN_FERDES_SIKKERT,
    DRIVAGGREGAT_KAN_FERDES_SIKKERT_BEGRUNNELSE,
    DRIVAGGREGAT_KAN_KJØRE_SELVSTENDIG_MED_JOYSTICK,
    DRIVAGGREGAT_KAN_KJØRE_SELVSTENDIG_MED_JOYSTICK_BEGRUNNELSE,
    DRIVAGGREGAT_KAN_MONTERE_OG_DEMONTERE_SELV,
    DRIVAGGREGAT_KAN_MONTERE_OG_DEMONTERE_SELV_BEGRUNNELSE,
    DRIVAGGREGAT_KAN_MONTERE_OG_DEMONTERE_VED_BILTRANSPORT,
    DRIVAGGREGAT_KAN_MONTERE_OG_DEMONTERE_VED_BILTRANSPORT_BEGRUNNELSE,
    DRIVAGGREGAT_LEDSAGERSTYRING_IKKE_NØDVENDIG_BEGRUNNELSE,
    DRIVAGGREGAT_SKAL_TRANSPORTERES_I_BIL,
    DRIVAGGREGAT_VALGT_RULLESTOL_SERIENR,
    ERS_BEHOV_FOR_KABIN,
    ERS_BEHOV_FOR_KABIN_BEGRUNNELSE,
    ERS_BETJENE_MANUELL_STYRING,
    ERS_BETJENE_MOTORISERT_STYRING,
    ERS_ENKLERE_LØSNING_VURDERT,
    ERS_FERDES_SIKKERT_I_TRAFIKKEN,
    ERS_HAR_SPESIALSYKKEL_FRA_FØR,
    ERS_HENDELPLASSERING,
    ERS_KJENT_MED_FORSIKRINGSVILKÅR,
    ERS_NEDSATT_GANGFUNKSJON,
    ERS_OPPBEVARING_OG_LADING,
    GANGHJELPEMIDDEL_HOVEDFORMÅL,
    GANGHJELPEMIDDEL_KAN_IKKE_BRUKE_MINDRE_AVANSERT,
    GANGHJELPEMIDDEL_PLAN,
    GODKJENNINGSKURS,
    KAN_IKKE_HA_TILSVARENDE_BEGRUNNELSE,
    KOMFYRVAKT_BEHOV_BEGRUNNELSE,
    KOMFYRVAKT_ER_KRAV,
    LAVERE_RANGERING_BEGRUNNELSE,
    LØFTEPUTE_BEHOV_BEGRUNNELSE,
    LØFTEPUTE_HAR_PROBLEMER_MED_Å_KOMME_SEG_OPP,
    LØFTEPUTE_KROPPSVEKT,
    MADRASS_OVER_120KG,
    MADRASS_OVER_120KG_BEGRUNNELSE,
    OPPREISNINGSSTOL_ANNET_TREKK,
    OPPREISNINGSSTOL_BEHOV,
    OPPREISNINGSSTOL_BEHOV_FOR_TILT,
    OPPREISNINGSSTOL_BRUKSOMRÅDE,
    OPPREISNINGSSTOL_KAN_REISE_SEG_SELV,
    OPPREISNINGSSTOL_LØFTTYPE,
    PERSONLØFTER_BEHOV_FOR_SEIL_ELLER_SELE,
    POSISJONERINGSSYSTEM_BEHOV,
    POSISJONERINGSSYSTEM_FORMIDLER_BEKREFTER,
    POSISJONERINGSSYSTEM_OPPGAVER_I_DAGLIGLIVET,
    POSISJONERINGSPUTE_BARN_BRUKSOMRÅDE,
    POSISJONERINGSPUTE_BARN_PLAN,
    RULLESTOL_BEHOV_FOR_SITTEPUTE,
    RULLESTOL_KROPPSMÅL,
    RULLESTOL_SKAL_BRUKES_I_BIL,
    SENG_BEHOV_BEGRUNNELSE,
    SENG_HØY_GRIND_ANDRE_TILTAK,
    SENG_HØY_GRIND_ANDRE_TILTAK_BEGRUNNELSE,
    SENG_HØY_GRIND_ER_LAGET_PLAN,
    SENG_HØY_GRIND_ER_LAGET_PLAN_BEGRUNNELSE,
    SENG_HØY_GRIND_TVANGSASPEKT,
    SENG_TRENGER_MADRASS,
    SITTEPUTE_SKAL_BRUKES_I_RULLESTOL_FRA_NAV,
    SITTEPUTE_TRYKKSÅRFOREBYGGING,
    STØTTESTANG_TAKHØYDE,
    TANNBØRSTE_BEHOV_BEGRUNNELSE,
    TANNBØRSTE_HAR_PRØVD_PUSSBEGER,
    TANNBØRSTE_KAN_FÅ_VANN_UT_AV_MUNNEN,
    TANNBØRSTE_PUSSBEGER_DEKKER_IKKE_BEHOV_BEGRUNNELSE,
    TANNBØRSTE_PUSSBEGER_IKKE_PRØVD_BEGRUNNELSE,
    TILBEHØR_BEGRUNNELSE,
    TILBEHØR_SKAL_BRUKES_MED,
    TRENGER_EN_TIL_BEGRUNNELSE,
    UTLEVERT_TYPE,
    VARMEHJM_OPPLYSNINGER_FRA_LEGE,
    VARMEHJM_OPPLYSNINGER_FRA_LEGE_VEDLEGG,
    VENDESYSTEM_SENG_ARTNR,
    VENDESYSTEM_SENG_BREDDE,
}