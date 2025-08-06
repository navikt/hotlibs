package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) =
    bearerAuth(tokenSet.accessToken)

internal val UserTokenKey =
    AttributeKey<String>("UserToken")

internal fun HttpRequestBuilder.userToken(): String? =
    attributes.getOrNull(UserTokenKey)

/**
 * Sett [userToken] for request. Gjør at [TokenSetProvider] forsøker token exchange (med mindre
 * [TokenExchangePreventionToken] er satt).
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.userToken(userToken: String) {
    attributes[UserTokenKey] = userToken
}

internal data object TokenExchangePreventionToken

internal val TokenExchangePreventionTokenKey =
    AttributeKey<TokenExchangePreventionToken>("TokenExchangePreventionToken")

internal fun HttpRequestBuilder.tokenExchangePreventionToken(): TokenExchangePreventionToken? =
    attributes.getOrNull(TokenExchangePreventionTokenKey)

/**
 * Ikke gjør token exchange, selv om [UserTokenKey] eller [UserContext] er satt.
 *
 * Kan bla. brukes for tjenester vi stort sett kaller på vegne av bruker, men som har spesifikke endepunkter som
 * ikke bruker har tilgang til, f.eks. PDLs bulkoppslag.
 *
 * @see [HttpRequestBuilder.userToken]
 * @see [UserContext]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.preventTokenExchange() {
    attributes[TokenExchangePreventionTokenKey] = TokenExchangePreventionToken
}
