package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import no.nav.hjelpemidler.http.context.currentRequestContext
import no.nav.hjelpemidler.security.ApplicationPrincipal
import no.nav.hjelpemidler.security.UserPrincipal

/**
 * Hent [TokenSet] (evt. basert på request/context).
 *
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 * @see [io.ktor.client.request.HttpRequestBuilder.onBehalfOf]
 * @see [io.ktor.client.request.HttpRequestBuilder.asApplication]
 * @see [no.nav.hjelpemidler.http.context.RequestContext.principal]
 */
fun interface TokenSetProvider {
    suspend operator fun invoke(request: HttpRequestBuilder): TokenSet
}

internal abstract class AbstractTokenSetProvider(
    protected val client: TexasClient,
    protected val identityProvider: IdentityProvider,
    protected val defaultTarget: String,
) : TokenSetProvider {
    override suspend fun invoke(request: HttpRequestBuilder): TokenSet {
        val target = request.attributes.getOrNull(TargetKey)?.toString() ?: defaultTarget
        return invoke(request, target)
    }

    protected abstract suspend fun invoke(request: HttpRequestBuilder, target: String): TokenSet
}

/**
 * [TokenSetProvider] som alltid henter Machine-To-Machine-token (M2M-token) for [defaultTarget] (eller overstyrt target).
 *
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 */
internal class ApplicationTokenSetProvider(
    client: TexasClient,
    identityProvider: IdentityProvider,
    defaultTarget: String,
) : AbstractTokenSetProvider(client, identityProvider, defaultTarget) {
    override suspend fun invoke(request: HttpRequestBuilder, target: String): TokenSet {
        return client.token(identityProvider, target)
    }
}

/**
 * [TokenSetProvider] som alltid henter On-Behalf-Of-token (OBO-token) for [defaultTarget] (eller overstyrt target).
 *
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 * @see [io.ktor.client.request.HttpRequestBuilder.onBehalfOf]
 * @see [no.nav.hjelpemidler.http.context.RequestContext.principal]
 */
internal class UserTokenSetProvider(
    client: TexasClient,
    identityProvider: IdentityProvider,
    defaultTarget: String,
) : AbstractTokenSetProvider(client, identityProvider, defaultTarget) {
    override suspend fun invoke(request: HttpRequestBuilder, target: String): TokenSet {
        val userToken = request.attributes.getOrNull(UserTokenKey)?.toString()
            ?: (currentRequestContext()?.principal as? UserPrincipal)?.userToken
            ?: error("userToken mangler")
        return client.exchange(identityProvider, target, userToken.toString())
    }
}

/**
 * [TokenSetProvider] som henter Machine-To-Machine-token (M2M-token) eller On-Behalf-Of-token (OBO-token)
 * for [defaultTarget] (eller overstyrt target) avhengig av kontekst.
 *
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 * @see [io.ktor.client.request.HttpRequestBuilder.onBehalfOf]
 * @see [io.ktor.client.request.HttpRequestBuilder.asApplication]
 * @see [no.nav.hjelpemidler.http.context.RequestContext.principal]
 */
internal class DelegatingTokenSetProvider(
    client: TexasClient,
    identityProvider: IdentityProvider,
    defaultTarget: String,
) : TokenSetProvider {
    private val application: TokenSetProvider = ApplicationTokenSetProvider(client, identityProvider, defaultTarget)
    private val user: TokenSetProvider = UserTokenSetProvider(client, identityProvider, defaultTarget)

    override suspend fun invoke(request: HttpRequestBuilder): TokenSet = when {
        request.attributes.getOrNull(AsApplicationKey) != null -> application(request)
        currentRequestContext()?.principal is ApplicationPrincipal -> application(request)
        else -> user(request)
    }
}
