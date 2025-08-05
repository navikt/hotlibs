package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.util.AttributeKey

fun interface TokenSetProvider : suspend (HttpRequestBuilder) -> TokenSet

internal data object TokenExchangePreventionToken

internal val UserTokenKey =
    AttributeKey<String>("UserToken")

internal val TokenExchangePreventionTokenKey =
    AttributeKey<TokenExchangePreventionToken>("TokenExchangePreventionToken")

internal fun HttpRequestBuilder.userToken(): String? =
    attributes.getOrNull(UserTokenKey)

fun HttpRequestBuilder.userToken(userToken: String) {
    attributes[UserTokenKey] = userToken
}

internal fun HttpRequestBuilder.tokenExchangePreventionToken(): TokenExchangePreventionToken? =
    attributes.getOrNull(TokenExchangePreventionTokenKey)
