package no.nav.hjelpemidler.security

import java.security.Principal

sealed interface AuthenticatedPrincipal : Principal

@JvmInline
value class AuthenticatedApplication(private val name: String) : AuthenticatedPrincipal {
    override fun getName(): String = name
}

abstract class AuthenticatedUser(private val name: String, val userToken: String) : AuthenticatedPrincipal {
    override fun getName(): String = name
}
