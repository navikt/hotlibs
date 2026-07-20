package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.currentCoroutineContext

private val log = KotlinLogging.logger {}

class TokenSetProviderFactory internal constructor(private val client: TexasClient) {
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
     * `userToken` defineres med [io.ktor.client.request.HttpRequestBuilder.onBehalfOf] eller hentes fra [AuthenticationContext].
     *
     * @see [HttpRequestBuilder.target]
     * @see [HttpRequestBuilder.onBehalfOf]
     * @see [AuthenticationContext]
     */
    fun userProvider(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider =
        TokenSetProvider { request ->
            val userToken = request.userToken ?: currentPrincipal()?.userToken ?: error("userToken mangler")
            client.exchange(identityProvider, request.target ?: defaultTarget, userToken)
        }

    /**
     * [TokenSetProvider] som henter Machine-To-Machine-token (M2M-token) eller On-Behalf-Of-token (OBO-token)
     * for [defaultTarget] (eller overstyrt target) avhengig av [HttpRequestBuilder]/[AuthenticationContext].
     *
     * @see [HttpRequestBuilder.target]
     * @see [HttpRequestBuilder.asApplication]
     * @see [HttpRequestBuilder.onBehalfOf]
     * @see [AuthenticationContext]
     */
    fun delegateProvider(identityProvider: IdentityProvider, defaultTarget: String): TokenSetProvider {
        val applicationProvider = applicationProvider(identityProvider, defaultTarget)
        val userProvider = userProvider(identityProvider, defaultTarget)
        return TokenSetProvider { request ->
            val principal = currentPrincipal()
            val (provider, reason) = when {
                request.asApplication -> applicationProvider to "request.asApplication er satt"
                !request.userToken.isNullOrBlank() -> userProvider to "request.userToken er satt"
                principal is AuthenticatedApplication -> applicationProvider to "principal er applikasjon"
                principal is AuthenticatedUser -> userProvider to "principal er bruker"
                else -> applicationProvider to "fallback til applicationProvider"
            }
            log.debug { "$reason, identityProvider: '$identityProvider', defaultTarget: '$defaultTarget'" }
            provider(request)
        }
    }

    private suspend fun currentPrincipal(): AuthenticatedPrincipal? =
        currentCoroutineContext().currentAuthenticatedPrincipal()
}

private val HttpRequestBuilder.target: String? get() = attributes.getOrNull(TargetValue.KEY)?.toString()
private val HttpRequestBuilder.asApplication: Boolean get() = attributes.getOrNull(AsApplicationValue.KEY) != null
private val HttpRequestBuilder.userToken: String? get() = attributes.getOrNull(UserTokenValue.KEY)?.toString()
