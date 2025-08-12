package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.currentCoroutineContext

private val log = KotlinLogging.logger {}

class TokenSetProviderFactory internal constructor(private val client: TexasClient) {
    private suspend fun openIDContext() =
        currentCoroutineContext()[OpenIDContext] ?: OpenIDContext(asApplication = false, userToken = null)

    /**
     * [TokenSetProvider] som alltid henter Machine-To-Machine-token (M2M-token) for [defaultTarget] (eller overstyrt target).
     *
     * @see [HttpRequestBuilder.target]
     */
    fun applicationProvider(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
        TokenSetProvider { request ->
            client.token(identityProvider, request.target ?: defaultTarget)
        }

    /**
     * [TokenSetProvider] som alltid henter On-Behalf-Of-token (OBO-token) for [defaultTarget] (eller overstyrt target).
     * `userToken` defineres med [io.ktor.client.request.HttpRequestBuilder.onBehalfOf] eller hentes fra [OpenIDContext].
     *
     * @see [HttpRequestBuilder.target]
     * @see [HttpRequestBuilder.onBehalfOf]
     * @see [OpenIDContext]
     */
    fun userProvider(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
        TokenSetProvider { request ->
            val userToken = request.userToken ?: openIDContext().userToken ?: error("userToken mangler")
            client.exchange(identityProvider, request.target ?: defaultTarget, userToken)
        }

    /**
     * [TokenSetProvider] som henter Machine-To-Machine-token (M2M-token) eller On-Behalf-Of-token (OBO-token)
     * for [defaultTarget] (eller overstyrt target) avhengig av [HttpRequestBuilder]/[OpenIDContext].
     *
     * @see [HttpRequestBuilder.target]
     * @see [HttpRequestBuilder.onBehalfOf]
     * @see [HttpRequestBuilder.asApplication]
     * @see [OpenIDContext]
     */
    fun delegateProvider(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider {
        val applicationProvider = applicationProvider(identityProvider, defaultTarget)
        val userProvider = userProvider(identityProvider, defaultTarget)
        return TokenSetProvider { request ->
            val openIDContext = openIDContext()
            val (provider, reason) = when {
                request.asApplication -> applicationProvider to "request.asApplication er sann"
                !request.userToken.isNullOrBlank() -> userProvider to "request.userToken er satt"
                openIDContext.asApplication -> applicationProvider to "openIDContext.asApplication er sann"
                !openIDContext.userToken.isNullOrBlank() -> userProvider to "openIDContext.userToken er satt"
                else -> applicationProvider to "fallback til applicationProvider"
            }
            log.debug { "$reason, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
            provider(request)
        }
    }
}

private val HttpRequestBuilder.target: String? get() = attributes.getOrNull(TargetKey)?.toString()
private val HttpRequestBuilder.asApplication: Boolean get() = attributes.getOrNull(AsApplicationKey) != null
private val HttpRequestBuilder.userToken: String? get() = attributes.getOrNull(UserTokenKey)?.toString()
