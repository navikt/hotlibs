package no.nav.hjelpemidler.http.openid

import java.security.Principal

sealed interface AuthenticatedPrincipal : Principal

@JvmInline
value class AuthenticatedApplication(private val name: String) : AuthenticatedPrincipal {
    override fun getName(): String = name
}

interface AuthenticatedUser : AuthenticatedPrincipal {
    val userToken: String
}

internal val AuthenticatedPrincipal.userToken: String? get() = (this as? AuthenticatedUser)?.userToken
