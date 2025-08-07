package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) = bearerAuth(tokenSet.accessToken)

internal val UserTokenKey = AttributeKey<String>("UserToken")

internal fun HttpRequestBuilder.userToken(): String? = attributes.getOrNull(UserTokenKey)

/**
 * Sett [UserTokenKey] for request. Gjør at [TexasTokenSetProvider] forsøker token exchange (med mindre
 * [TokenExchangePreventionToken] er satt).
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.userToken(userToken: String) {
    attributes[UserTokenKey] = userToken
}

/**
 * Sett [UserTokenKey] for request. Gjør at [TexasTokenSetProvider] forsøker token exchange (med mindre
 * [TokenExchangePreventionToken] er satt).
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.userToken(userToken: DecodedJWT) = userToken(userToken.token)

internal val TargetKey = AttributeKey<String>("Target")

internal fun HttpRequestBuilder.target(): String? = attributes.getOrNull(TargetKey)

/**
 * Overstyr `target` for request.
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.target(target: String) {
    attributes[TargetKey] = target
}

internal data object TokenExchangePreventionToken

internal val TokenExchangePreventionTokenKey =
    AttributeKey<TokenExchangePreventionToken>("TokenExchangePreventionToken")

internal fun HttpRequestBuilder.tokenExchangePreventionToken(): TokenExchangePreventionToken? =
    attributes.getOrNull(TokenExchangePreventionTokenKey)

/**
 * Ikke gjør token exchange, selv om [UserTokenKey] eller [UserContext] er satt.
 *
 * Kan bla. brukes for tjenester hvor vi stort sett kaller på vegne av bruker, men som har spesifikke endepunkter som
 * ikke bruker har tilgang til, f.eks. PDLs bulkoppslag.
 *
 * @see [HttpRequestBuilder.userToken]
 * @see [UserContext]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.preventTokenExchange() {
    attributes[TokenExchangePreventionTokenKey] = TokenExchangePreventionToken
}
