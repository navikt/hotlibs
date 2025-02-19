package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

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
    UGRADERT,

    @JsonEnumDefaultValue
    UKJENT,
}
