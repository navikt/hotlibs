package no.nav.hjelpemidler.http.openid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class OpenIDContext(
    val accessToken: String? = null,
) : AbstractCoroutineContextElement(OpenIDContext) {
    companion object Key : CoroutineContext.Key<OpenIDContext>
}

fun CoroutineContext.openIDContext(): OpenIDContext =
    get(OpenIDContext.Key) ?: OpenIDContext(accessToken = null)

suspend fun <T> withOpenIDContext(accessToken: String? = null, block: suspend CoroutineScope.() -> T): T =
    withContext(currentCoroutineContext() + OpenIDContext(accessToken = accessToken), block)
