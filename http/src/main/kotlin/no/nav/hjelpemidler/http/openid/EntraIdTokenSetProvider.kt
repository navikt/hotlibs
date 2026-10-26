package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedUser

private val log = KotlinLogging.logger {}

/**
 * [TokenSetProvider] som henter Machine-To-Machine-token (M2M-token) eller On-Behalf-Of-token (OBO-token) for
 * [defaultTarget] (eller overstyrt target) avhengig av [io.ktor.client.request.HttpRequestBuilder]/[no.nav.hjelpemidler.security.AuthenticationContext].
 *
 * @see [HttpRequestBuilder.target]
 * @see [HttpRequestBuilder.asApplication]
 * @see [onBehalfOf]
 * @see [no.nav.hjelpemidler.security.AuthenticationContext]
 */
internal class EntraIdTokenSetProvider(texas: Texas, defaultTarget: String) :
    TexasTokenSetProvider(texas, IdentityProvider.ENTRA_ID, defaultTarget) {
    private val applicationProvider: TokenSetProvider =
        ApplicationTokenSetProvider(texas, identityProvider, defaultTarget)
    private val userProvider: TokenSetProvider =
        UserTokenSetProvider(texas, identityProvider, defaultTarget)

    override suspend fun invoke(request: HttpRequestBuilder): TokenSet {
        val principal = currentPrincipal()
        val (provider, reason) = when {
            request.asApplication -> applicationProvider to "request.asApplication er satt"
            !request.userToken.isNullOrBlank() -> userProvider to "request.userToken er satt"
            principal is AuthenticatedApplication -> applicationProvider to "principal er AuthenticatedApplication"
            principal is AuthenticatedUser -> userProvider to "principal er AuthenticatedUser"
            else -> applicationProvider to "fallback til applicationProvider"
        }
        log.debug { "$provider -> '$reason'" }
        return provider(request)
    }

    override fun toString(): String = "EntraIdTokenSetProvider(defaultTarget: $defaultTarget)"
}
