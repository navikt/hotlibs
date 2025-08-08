package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder

private val log = KotlinLogging.logger {}

/**
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 * @see [io.ktor.client.request.HttpRequestBuilder.påVegneAv]
 * @see [io.ktor.client.request.HttpRequestBuilder.systembruker]
 */
internal class TexasTokenSetProvider(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
    private val defaultTarget: Target,
) : TokenSetProvider {
    override suspend fun invoke(request: HttpRequestBuilder): TokenSet {
        val target = request.attributes.getOrNull(TargetKey) ?: defaultTarget
        val userToken = request.attributes.getOrNull(UserTokenKey) ?: currentUserContext()?.userToken
        val systembrukerToken = request.attributes.getOrNull(SystembrukerTokenKey)
        return if (userToken != null && systembrukerToken == null) {
            log.debug { "Brukerkontekst, gjør token exchange, identityProvider: '$identityProvider', target: '$target'" }
            client.exchange(identityProvider, target.toString(), userToken.toString())
        } else {
            log.debug {
                val message = if (userToken == null) "Systembrukerkontekst" else "Brukerkontekst som systembruker"
                "$message, henter token, identityProvider: '$identityProvider', target: '$target'"
            }
            client.token(identityProvider, target.toString())
        }
    }
}
