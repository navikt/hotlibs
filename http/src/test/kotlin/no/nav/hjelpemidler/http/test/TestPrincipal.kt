package no.nav.hjelpemidler.http.test

import no.nav.hjelpemidler.http.context.ApplicationPrincipal
import no.nav.hjelpemidler.http.context.UserPrincipal
import no.nav.hjelpemidler.http.openid.Token

data object TestApplicationPrincipal : ApplicationPrincipal {
    override fun getName(): String = toString()
}

data object TestUserPrincipal : UserPrincipal {
    override fun getName(): String = toString()
    override val userToken: Token = Token("userTokenFromContext")
}
