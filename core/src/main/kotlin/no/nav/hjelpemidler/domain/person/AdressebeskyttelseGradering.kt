package no.nav.hjelpemidler.domain.person

/**
 * @see <a href="https://pdl-docs.ansatt.nav.no/ekstern/index.html#_adressebeskyttelse_2">Persondataløsningen (PDL) - Adressebeskyttelse</a>
 */
enum class AdressebeskyttelseGradering(internal val presedens: Int) {
    /**
     * Paragraf 19 i Bisys. Behandles tilsvarende [STRENGT_FORTROLIG].
     *
     * Master: PDL
     */
    STRENGT_FORTROLIG_UTLAND(presedens = 1),

    /**
     * Tidligere kjent som kode 6.
     *
     * Master: Folkeregisteret
     */
    STRENGT_FORTROLIG(presedens = 2),

    /**
     * Tidligere kjent som kode 7.
     *
     * Master: Folkeregisteret
     */
    FORTROLIG(presedens = 3),

    /**
     * Benyttes ikke i PDL pt., men kan tas i bruk av Folkeregisteret på et senere tidspunkt. Ved ugradert får man i dag et tomt sett fra PDL.
     */
    UGRADERT(presedens = Int.MAX_VALUE),
    ;

    val isStrengtFortrolig: Boolean get() = this == STRENGT_FORTROLIG_UTLAND || this == STRENGT_FORTROLIG
    val isFortrolig: Boolean get() = this == FORTROLIG
    val isGradert: Boolean get() = isStrengtFortrolig || isFortrolig
}

val AdressebeskyttelseGradering?.isStrengtFortrolig: Boolean get() = this?.isStrengtFortrolig == true
val AdressebeskyttelseGradering?.isFortrolig: Boolean get() = this?.isFortrolig == true
val AdressebeskyttelseGradering?.isGradert: Boolean get() = this?.isGradert == true

/**
 * Hvis vi skulle få flere graderinger fra PDL (ingen tilfeller pt.), så er det viktig at vi velger gjeldende gradering basert på presedens.
 *
 * Presedens:
 * 1. [AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND]
 * 2. [AdressebeskyttelseGradering.STRENGT_FORTROLIG]
 * 3. [AdressebeskyttelseGradering.FORTROLIG]
 * 4. [AdressebeskyttelseGradering.UGRADERT]
 */
val Set<AdressebeskyttelseGradering>.gjeldende: AdressebeskyttelseGradering
    get() = minByOrNull(AdressebeskyttelseGradering::presedens) ?: AdressebeskyttelseGradering.UGRADERT
