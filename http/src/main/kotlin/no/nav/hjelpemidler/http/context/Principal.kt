package no.nav.hjelpemidler.http.context

import no.nav.hjelpemidler.http.openid.Token

sealed interface Principal : java.security.Principal

interface ApplicationPrincipal : Principal

interface UserPrincipal : Principal {
    /**
     * Token for token exchange.
     */
    val userToken: Token
}
