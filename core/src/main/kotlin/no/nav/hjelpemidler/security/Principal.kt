package no.nav.hjelpemidler.security

interface Principal<ID : Any> {
    val id: ID
}

/**
 * [Principal] er applikasjon, også kalt "systembruker".
 */
interface ApplicationPrincipal : Principal<String>

/**
 * [Principal] er person.
 */
interface UserPrincipal<ID : Any> : Principal<ID> {
    /**
     * Token for token exchange.
     */
    val userToken: String
}
