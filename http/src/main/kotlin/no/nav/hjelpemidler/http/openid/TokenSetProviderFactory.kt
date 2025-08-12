package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.currentCoroutineContext

private val log = KotlinLogging.logger {}

class TokenSetProviderFactory internal constructor(private val client: TexasClient) {
    private suspend fun openIDContext() = currentCoroutineContext()[OpenIDContext]

    /**
     * [TokenSetProvider] som alltid henter Machine-To-Machine-token (M2M-token) for [defaultTarget] (eller overstyrt target).
     *
     * @see [HttpRequestBuilder.target]
     */
    fun application(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
        TokenSetProvider { request ->
            val target = request.attributes.getOrNull(TargetKey)?.toString() ?: defaultTarget
            client.token(identityProvider, target)
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
            val target = request.attributes.getOrNull(TargetKey)?.toString() ?: defaultTarget
            val userToken = request.attributes.getOrNull(UserTokenKey)?.toString()
                ?: openIDContext()?.userToken
                ?: error("userToken mangler")
            client.exchange(identityProvider, target, userToken)
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
            when {
                request.attributes.getOrNull(AsApplicationKey) != null -> {
                    log.debug { "AsApplicationKey != null, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    application(request)
                }

                openIDContext()?.userIsApplication == true -> {
                    log.debug { "userIsApplication == true, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    application(request)
                }

                else -> {
                    log.debug { "userIsApplication != true, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
                    user(request)
                }
            }
        }
    }
}
