package no.nav.hjelpemidler.http.context

import kotlinx.coroutines.currentCoroutineContext
import no.nav.hjelpemidler.security.Principal
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class RequestContext(val principal: Principal) : AbstractCoroutineContextElement(RequestContext) {
    companion object Key : CoroutineContext.Key<RequestContext>
}

suspend fun currentRequestContext(): RequestContext? = currentCoroutineContext()[RequestContext]
