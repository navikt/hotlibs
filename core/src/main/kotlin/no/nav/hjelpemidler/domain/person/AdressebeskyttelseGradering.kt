package no.nav.hjelpemidler.domain.person

/**
 * @see <a href="https://pdl-docs.ansatt.nav.no/ekstern/index.html#_adressebeskyttelse_2">Persondataløsningen (PDL) - Adressebeskyttelse</a>
 */
enum class AdressebeskyttelseGradering(internal val order: Int) {
    /**
     * Tilsvarer paragraf 19 i Bisys (henvisning til Forvaltningslovens §19).
     *
     * Fra PDLs dokumentasjon (om *Strengt Fortrolig Utland*):
     *
     * *Ved strengt fortrolig utland behandles personen i Nav tilsvarende som ved graderingen strengt fortrolig fra Folkeregisteret. Ansatte ved Nav Vikafossen kan registrere koden. Personer med strengt fortrolig utland kan ha adresser registrert i PDL, slik at de kan kontaktes av Nav ved behov. Det er kun de med egen tilgang som skal kunne få opp adressen på disse personene.*
     */
    STRENGT_FORTROLIG_UTLAND(order = 1),

    /**
     * Tidligere spesregkode kode 6 fra TPS. Gradering strengt fortrolig i henhold til Beskyttelsesinstruksen.
     *
     * Fra PDLs dokumentasjon (om *Strengt Fortrolig*):
     *
     * *Adressebeskyttelse tildeles trusselutsatte personer hvor det foreligger fare for skade på liv, legeme eller helse. Når en person får denne adressebeskyttelsen så vil alle i husstanden få samme gradering, og de må samtidig flytte. Denne graderingen innebærer at ingen i Nav vil kunne se bostedsadressen til vedkommende, da PDL ikke mottar bostedsadressen fra Freg. Personen vil kunne ha en kontaktadresse som de med egen tilgang kan se.*
     */
    STRENGT_FORTROLIG(order = 2),

    /**
     * Tidligere spesregkode kode 7 fra TPS. Graderingen fortrolig i henhold til Beskyttelsesinstruksen.
     *
     * Fra PDLs dokumentasjon (om *Fortrolig*):
     *
     * *Adressebeskyttelse tildeles trusselutsatte personer. Denne graderingen innebærer at kun ansatte i Nav med egen tilgang for dette, vil kunne se bostedsadressen til vedkommende. Adressen gis ikke til private og institusjoner uten lovhjemlet rett til opplysningene.*
     */
    FORTROLIG(order = 3),

    /**
     * Ingen adressebeskyttelse.
     *
     * Fra PDLs dokumentasjon (om *Ugradert*):
     *
     * *Kode vi kan få fra Folkeregisteret. Vi har ingen tilfeller per i dag i produksjon. Det vanligste fra PDL er å få et tomt svar (tom liste). Ugradert betyr at man ikke har noen adressebeskyttelse, og er altså det samme som at feltet er tomt.*
     */
    UGRADERT(order = Int.MAX_VALUE),
    ;

    val isStrengtFortrolig: Boolean get() = this == STRENGT_FORTROLIG_UTLAND || this == STRENGT_FORTROLIG
    val isFortrolig: Boolean get() = this == FORTROLIG
    val isGradert: Boolean get() = isStrengtFortrolig || isFortrolig
}

val AdressebeskyttelseGradering?.isStrengtFortrolig: Boolean get() = this?.isStrengtFortrolig == true
val AdressebeskyttelseGradering?.isFortrolig: Boolean get() = this?.isFortrolig == true
val AdressebeskyttelseGradering?.isGradert: Boolean get() = this?.isGradert == true

/**
 * NB! Ingen personer har mer en én gradering pt. Hvis vi skulle få flere verdier på et tidspunkt, velger vi den strengeste basert på [AdressebeskyttelseGradering.order].
 *
 * Fra PDLs dokumentasjon (om *Parallelle verdier på adressebeskyttelse*):
 *
 * *Adressebeskyttelse vil bare kunne registreres med PDL som master på typen Strengt Fortrolig Utland. Mens de andre typene kun kan ha master Freg. Strengt Fortrolig Utland skal ikke registreres dersom personen allerede har adressebeskyttelse Fortrolig eller Strengt Fortrolig. Så lenge ikke Freg benytter typen Ugradert så vil det ikke være mulig å ha to typer adressebeskyttelser som er gyldige samtidig. Så det skal ikke forekomme parallelle verdier med mindre Freg tar i bruk Ugradert. Dersom det oppstår så skal man benytte Strengt Fortrolig Utland og ikke Ugradert.*
 */
val Set<AdressebeskyttelseGradering>.strengeste: AdressebeskyttelseGradering
    get() = minByOrNull(AdressebeskyttelseGradering::order) ?: AdressebeskyttelseGradering.UGRADERT
