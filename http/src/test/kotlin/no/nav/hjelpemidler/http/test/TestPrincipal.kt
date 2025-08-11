package no.nav.hjelpemidler.http.test

import no.nav.hjelpemidler.security.ApplicationPrincipal
import no.nav.hjelpemidler.security.UserPrincipal

data object TestApplicationPrincipal : ApplicationPrincipal {
    override val id: String get() = toString()
}

data object TestUserPrincipal : UserPrincipal<String> {
    override val id: String get() = toString()
    override val userToken: String = "userTokenFromContext"
}
