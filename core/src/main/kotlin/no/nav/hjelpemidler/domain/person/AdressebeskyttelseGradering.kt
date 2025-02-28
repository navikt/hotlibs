package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering.Kategori

/**
 * @see <a href="https://pdl-docs.ansatt.nav.no/ekstern/index.html#_adressebeskyttelse_2">Persondataløsningen (PDL) - Adressebeskyttelse</a>
 */
enum class AdressebeskyttelseGradering(val kategori: Kategori) {
    /**
     * Tidligere diskresjonskode 6 + utland.
     */
    STRENGT_FORTROLIG_UTLAND(Kategori.STRENGT_FORTROLIG),

    /**
     * Tidligere diskresjonskode 6.
     */
    STRENGT_FORTROLIG(Kategori.STRENGT_FORTROLIG),

    /**
     * Tidligere diskresjonskode 7.
     */
    FORTROLIG(Kategori.FORTROLIG),

    /**
     * NB! "Ingen tilfeller per i dag i produksjon" i følge Persondataløsningens (PDL) dokumentasjon.
     */
    UGRADERT(Kategori.UGRADERT),
    ;

    val strengtFortrolig: Boolean get() = kategori.strengtFortrolig
    val fortrolig: Boolean get() = kategori.fortrolig
    val gradert: Boolean get() = kategori.gradert

    enum class Kategori(internal val kode: Int) {
        STRENGT_FORTROLIG(kode = 6),
        FORTROLIG(kode = 7),
        UGRADERT(kode = Int.MAX_VALUE),
        ;

        val strengtFortrolig: Boolean get() = this == STRENGT_FORTROLIG
        val fortrolig: Boolean get() = this == FORTROLIG
        val gradert: Boolean get() = this != UGRADERT
    }
}

val AdressebeskyttelseGradering?.kategori: Kategori get() = this?.kategori ?: Kategori.UGRADERT

val AdressebeskyttelseGradering?.strengtFortrolig: Boolean get() = kategori.strengtFortrolig
val AdressebeskyttelseGradering?.fortrolig: Boolean get() = kategori.fortrolig
val AdressebeskyttelseGradering?.gradert: Boolean get() = kategori.gradert

/**
 * Finn strengeste kategori basert på laveste [Kategori.kode].
 */
val Collection<AdressebeskyttelseGradering>.kategori: Kategori
    get() = map(AdressebeskyttelseGradering::kategori).minByOrNull(Kategori::kode) ?: Kategori.UGRADERT

val Collection<AdressebeskyttelseGradering>.gradert: Boolean
    get() = kategori.gradert
