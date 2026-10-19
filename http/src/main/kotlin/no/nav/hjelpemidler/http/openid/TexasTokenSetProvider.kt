package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.currentCoroutineContext
import no.nav.hjelpemidler.security.AuthenticatedPrincipal
import no.nav.hjelpemidler.security.AuthenticatedUser
import no.nav.hjelpemidler.security.principal

internal sealed class TexasTokenSetProvider(
    protected val texas: Texas,
    protected val identityProvider: IdentityProvider,
    protected val defaultTarget: String,
) : TokenSetProvider {
    protected suspend fun currentPrincipal(): AuthenticatedPrincipal? = currentCoroutineContext().principal()
}

/**
 * [TokenSetProvider] som alltid henter Machine-To-Machine-token (M2M-token) for [defaultTarget] (eller overstyrt target).
 *
 * @see [HttpRequestBuilder.target]
 */
internal class ApplicationTokenSetProvider(texas: Texas, identityProvider: IdentityProvider, defaultTarget: String) :
    TexasTokenSetProvider(texas, identityProvider, defaultTarget) {
    override suspend fun invoke(request: HttpRequestBuilder): TokenSet {
        val target = request.target ?: defaultTarget
        return texas.token(identityProvider, target)
    }

    override fun toString(): String =
        "ApplicationTokenSetProvider(identityProvider: $identityProvider, defaultTarget: $defaultTarget)"
}

/**
 * [TokenSetProvider] som alltid henter On-Behalf-Of-token (OBO-token) for [defaultTarget] (eller overstyrt target).
 * `userToken` defineres med [HttpRequestBuilder.onBehalfOf] eller hentes fra [no.nav.hjelpemidler.security.AuthenticationContext].
 *
 * @see [HttpRequestBuilder.target]
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [no.nav.hjelpemidler.security.AuthenticationContext]
 */
internal class UserTokenSetProvider(texas: Texas, identityProvider: IdentityProvider, defaultTarget: String) :
    TexasTokenSetProvider(texas, identityProvider, defaultTarget) {
    override suspend fun invoke(request: HttpRequestBuilder): TokenSet {
        val userToken = request.userToken ?: currentPrincipal()?.userToken ?: error("userToken mangler")
        val target = request.target ?: defaultTarget
        return texas.exchange(identityProvider, target, userToken)
    }

    override fun toString(): String =
        "UserTokenSetProvider(identityProvider: $identityProvider, defaultTarget: $defaultTarget)"
}

internal val AuthenticatedPrincipal.userToken: String? get() = (this as? AuthenticatedUser)?.userToken

internal val HttpRequestBuilder.target: String? get() = attributes.getOrNull(TargetValue.KEY)?.toString()
internal val HttpRequestBuilder.asApplication: Boolean get() = attributes.getOrNull(AsApplicationValue.KEY) != null
internal val HttpRequestBuilder.userToken: String? get() = attributes.getOrNull(UserTokenValue.KEY)?.toString()
