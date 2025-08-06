package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.HttpRequestBuilder

private val log = KotlinLogging.logger {}

/**
 * [TokenSetProvider] for [identityProvider] som henter token eller gjør token exchange avhengig av
 * attributtene [userToken] og [tokenExchangePreventionToken]. `userToken` kan også defineres med [UserContext].
 *
 * @see [userToken]
 * @see [tokenExchangePreventionToken]
 */
internal class TexasTokenSetProvider(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
    private val target: String,
) : TokenSetProvider {
    override suspend fun invoke(builder: HttpRequestBuilder): TokenSet {
        val userToken = builder.userToken() ?: currentUserContext()?.userToken
        return if (userToken != null && builder.tokenExchangePreventionToken() == null) {
            log.debug { "userToken er satt, gjør token exchange, identityProvider: '$identityProvider', target: '$target'" }
            client.exchange(identityProvider, target, userToken)
        } else {
            log.debug { "userToken er ikke satt, henter token, identityProvider: '$identityProvider', target: '$target'" }
            client.token(identityProvider, target)
        }
    }
}
