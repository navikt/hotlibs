package no.nav.hjelpemidler.security

sealed interface Principal {
    val id: String
}

/**
 * [Principal] som er en applikasjon, også kalt "systembruker".
 */
interface ApplicationPrincipal : Principal

/**
 * [Principal] som er en person.
 */
interface UserPrincipal : Principal {
    /**
     * Token for token exchange.
     */
    val userToken: String
}
