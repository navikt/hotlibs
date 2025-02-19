package no.nav.hjelpemidler.domain.person

/**
 * @see <a href="https://pdl-docs.ansatt.nav.no/ekstern/index.html#_adressebeskyttelse_2">Persondataløsningen (PDL) - Adressebeskyttelse</a>
 */
enum class AdressebeskyttelseGradering {
    /**
     * Tidligere diskresjonskode 6 + utland.
     */
    STRENGT_FORTROLIG_UTLAND,

    /**
     * Tidligere diskresjonskode 6.
     */
    STRENGT_FORTROLIG,

    /**
     * Tidligere diskresjonskode 7.
     */
    FORTROLIG,

    /**
     * NB! "Ingen tilfeller per i dag i produksjon" i følge Persondataløsningens (PDL) dokumentasjon.
     */
    UGRADERT,
}

/**
 * Tidligere diskresjonskode 6.
 *
 * @see [AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND]
 * @see [AdressebeskyttelseGradering.STRENGT_FORTROLIG]
 */
val Collection<AdressebeskyttelseGradering>.erStrengtFortrolig: Boolean
    get() = AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND in this || AdressebeskyttelseGradering.STRENGT_FORTROLIG in this

/**
 * Tidligere diskresjonskode 7.
 *
 * @see [AdressebeskyttelseGradering.FORTROLIG]
 */
val Collection<AdressebeskyttelseGradering>.erFortrolig: Boolean
    get() = AdressebeskyttelseGradering.FORTROLIG in this

/**
 * Ingen gradering eller kun [AdressebeskyttelseGradering.UGRADERT].
 *
 * @see [AdressebeskyttelseGradering.UGRADERT]
 */
val Collection<AdressebeskyttelseGradering>.erUgradert: Boolean
    get() = minus(AdressebeskyttelseGradering.UGRADERT).isEmpty()
