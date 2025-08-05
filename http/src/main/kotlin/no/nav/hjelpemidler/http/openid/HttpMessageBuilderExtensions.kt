package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) =
    bearerAuth(tokenSet.accessToken)

internal data object TokenExchangePreventionToken

internal val UserTokenKey =
    AttributeKey<String>("UserToken")

internal fun HttpRequestBuilder.userToken(): String? =
    attributes.getOrNull(UserTokenKey)

fun HttpRequestBuilder.userToken(userToken: String) {
    attributes[UserTokenKey] = userToken
}

internal val TokenExchangePreventionTokenKey =
    AttributeKey<TokenExchangePreventionToken>("TokenExchangePreventionToken")

internal fun HttpRequestBuilder.tokenExchangePreventionToken(): TokenExchangePreventionToken? =
    attributes.getOrNull(TokenExchangePreventionTokenKey)

fun HttpRequestBuilder.preventTokenExchange() {
    attributes[TokenExchangePreventionTokenKey] = TokenExchangePreventionToken
}
