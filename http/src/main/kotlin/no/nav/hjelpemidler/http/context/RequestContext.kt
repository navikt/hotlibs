package no.nav.hjelpemidler.http.context

import kotlinx.coroutines.currentCoroutineContext
import no.nav.hjelpemidler.security.Principal
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * @see <a href="https://blog.tpersson.io/2018/04/22/emulating-request-scoped-objects-with-kotlin-coroutines/">Mer informasjon.</a>
 */
class RequestContext(val principal: Principal<*>) : AbstractCoroutineContextElement(RequestContext) {
    companion object Key : CoroutineContext.Key<RequestContext>
}

suspend fun currentRequestContext(): RequestContext? = currentCoroutineContext()[RequestContext]
