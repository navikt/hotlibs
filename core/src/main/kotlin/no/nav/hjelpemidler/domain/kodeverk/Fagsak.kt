package no.nav.hjelpemidler.domain.kodeverk

/**
 * @see <a href="https://confluence.adeo.no/x/FUutEg">Opprett Journalpost</a>
 */
enum class Fagsaksystem(override val beskrivelse: String) : Kodeverk<Fagsaksystem> {
    AO01(beskrivelse = "Arena"),
    AO11(beskrivelse = "Grisen"),
    ARBEIDSOPPFOLGING(beskrivelse = "Arbeidsoppfølging"),
    BA(beskrivelse = "Barnetrygd"),
    BARNEBRILLER(beskrivelse = "Barnebriller"),
    BISYS(beskrivelse = "BISYS"),
    DAGPENGER(beskrivelse = "Dagpenger"),
    EF(beskrivelse = "Enslig forsørger"),
    EKSPERTBISTAND(beskrivelse = "Ekspertbistand"),
    EY(beskrivelse = "Etterlatteytelser"),
    FIA(beskrivelse = "FIA"),
    FS36(beskrivelse = "Foreldrepengeløsningen"),
    FS38(beskrivelse = "Melosys"),
    HELT(beskrivelse = "Helsetjenester"),
    HJELPEMIDLER(beskrivelse = "Hotsak"),
    IT01(beskrivelse = "Infotrygd"),
    K9(beskrivelse = "Sykdom i familien"),
    KELVIN(beskrivelse = "Arbeidsavklaringspenger"),
    KOMPYS(beskrivelse = "Yrkesskade"),
    KONT(beskrivelse = "Kontantstøtte"),
    NEESSI(beskrivelse = "NEESSI"),
    OB36(beskrivelse = "UR"),
    OEBS(beskrivelse = "OeBS"),
    OMSORGSPENGER(beskrivelse = "Omsorgspenger"),
    PP01(beskrivelse = "Pesys"),
    REMEDY(beskrivelse = "Identstyring"),
    SPEIL(beskrivelse = "Fagsystem sykepenger"),
    SUPERHELT(beskrivelse = "Fagsystem for diverse hjelpemidler"),
    SUPSTONAD(beskrivelse = "Supplerende stønad"),
    TILLEGGSSTONADER(beskrivelse = "Tilleggsstønader"),
    TILTAKSADMINISTRASJON(beskrivelse = "Tiltaksadministrasjon"),
    TILTAKSPENGER(beskrivelse = "Tiltakspenger"),
    UFM(beskrivelse = "Unntak fra medlemskap"),
    UNG_SAK(beskrivelse = "Fagsystem for å saksbehandling av ungdomsprogramytelsen"),
    WATSON(beskrivelse = "Fagsystem for Nav Kontroll"),
    ;
}

/**
 * @see <a href="https://confluence.adeo.no/x/FUutEg">Opprett Journalpost</a>
 */
enum class Fagsaktype(val value: String) : Kodeverk<Fagsaktype> {
    FAGSAK("FAGSAK"),
    GENERELL_SAK("GENERELL_SAK"),

    @Deprecated("Skal ikke brukes av konsumenter")
    ARKIVSAK("ARKIVSAK"),
    ;
}
