package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.currentCoroutineContext

private val log = KotlinLogging.logger {}

class TokenSetProviderFactory internal constructor(private val client: TexasClient) {
    private suspend fun openIDContext() =
        currentCoroutineContext()[OpenIDContext] ?: OpenIDContext(userIsApplication = false, userToken = null)

    /**
     * [TokenSetProvider] som alltid henter Machine-To-Machine-token (M2M-token) for [defaultTarget] (eller overstyrt target).
     *
     * @see [HttpRequestBuilder.target]
     */
    fun application(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
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
    fun user(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
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
    fun delegate(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider {
        val application = application(identityProvider, defaultTarget)
        val user = user(identityProvider, defaultTarget)
        return TokenSetProvider { request ->
            val openIDContext = openIDContext()
            when {
                request.asApplication -> {
                    log.debug { "request.asApplication er sann, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    application(request)
                }

                !request.userToken.isNullOrBlank() -> {
                    log.debug { "request.userToken er satt, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    user(request)
                }

                !openIDContext.userToken.isNullOrBlank() -> {
                    log.debug { "openIDContext.userToken er satt, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    user(request)
                }

                openIDContext.userIsApplication -> {
                    log.debug { "openIDContext.userIsApplication er sann, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    application(request)
                }

                else -> {
                    log.debug { "Fallback, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    application(request)
                }
            }
        }
    }
}

private val HttpRequestBuilder.target: String? get() = attributes.getOrNull(TargetKey)?.toString()
private val HttpRequestBuilder.userToken: String? get() = attributes.getOrNull(UserTokenKey)?.toString()
private val HttpRequestBuilder.asApplication: Boolean get() = attributes.getOrNull(AsApplicationKey) != null
