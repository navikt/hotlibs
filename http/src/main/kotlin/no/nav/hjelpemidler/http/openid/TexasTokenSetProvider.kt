package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder

private val log = KotlinLogging.logger {}

/**
 * [TokenSetProvider] for [identityProvider] som henter token eller gjør token exchange avhengig av
 * attributtene [UserTokenKey] og [TokenExchangePreventionTokenKey]. `userToken` kan også defineres med [UserContext].
 * `target` kan overstyres med [io.ktor.client.request.HttpRequestBuilder.target].
 *
 * @see [io.ktor.client.request.HttpRequestBuilder.target]
 * @see [io.ktor.client.request.HttpRequestBuilder.userToken]
 * @see [io.ktor.client.request.HttpRequestBuilder.preventTokenExchange]
 */
internal class TexasTokenSetProvider(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
    private val defaultTarget: String,
) : TokenSetProvider {
    override suspend fun invoke(builder: HttpRequestBuilder): TokenSet {
        val target = builder.target() ?: defaultTarget
        val userToken = builder.userToken() ?: currentUserContext()?.userToken
        return if (userToken != null && builder.tokenExchangePreventionToken() == null) {
            log.debug { "userToken er satt, gjør token exchange, identityProvider: '$identityProvider', target: '$target'" }
            client.exchange(identityProvider, target, userToken)
        } else {
            log.debug { "userToken er ikke satt (eller ignoreres), henter token, identityProvider: '$identityProvider', target: '$target'" }
            client.token(identityProvider, target)
        }
    }
}
