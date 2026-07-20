package no.nav.hjelpemidler.security

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class AuthenticationContext(val principal: AuthenticatedPrincipal) :
    AbstractCoroutineContextElement(AuthenticationContext) {
    companion object Key : CoroutineContext.Key<AuthenticationContext>
}

fun CoroutineContext.currentAuthenticatedPrincipal(): AuthenticatedPrincipal? = this[AuthenticationContext]?.principal
