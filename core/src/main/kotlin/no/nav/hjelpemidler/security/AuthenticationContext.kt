package no.nav.hjelpemidler.security

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * @see <a href="https://blog.tpersson.io/2018/04/22/emulating-request-scoped-objects-with-kotlin-coroutines/">Emulating request scoped objects with Kotlin Coroutines</a>
 */
class AuthenticationContext(val principal: AuthenticatedPrincipal) :
    AbstractCoroutineContextElement(AuthenticationContext) {
    companion object Key : CoroutineContext.Key<AuthenticationContext>
}

fun CoroutineContext.principal(): AuthenticatedPrincipal? = this[AuthenticationContext]?.principal
