package no.nav.hjelpemidler.security

sealed interface Principal<ID : Any> {
    val id: ID
}

/**
 * [Principal] som er en applikasjon, også kalt "systembruker".
 */
interface ApplicationPrincipal : Principal<String>

/**
 * [Principal] som er en person.
 */
interface UserPrincipal<ID : Any> : Principal<ID> {
    /**
     * Token for token exchange.
     */
    val userToken: String
}
