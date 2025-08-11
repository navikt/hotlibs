package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) = bearerAuth(tokenSet.accessToken)

internal val TargetKey = AttributeKey<Target>("Target")
internal val UserTokenKey = AttributeKey<Token>("UserToken")
internal val AsApplicationKey = AttributeKey<AsApplication>("AsApplication")

/**
 * Sett `target` for request.
 */
fun HttpRequestBuilder.target(target: Target) {
    attributes[TargetKey] = target
}

/**
 * Sett `target` for request.
 */
fun HttpRequestBuilder.target(target: String) = target(Target(target))

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.asApplication]
 */
fun HttpRequestBuilder.onBehalfOf(userToken: Token) {
    attributes[UserTokenKey] = userToken
}

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.asApplication]
 */
fun HttpRequestBuilder.onBehalfOf(userToken: String) = onBehalfOf(Token(userToken))

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.asApplication]
 */
fun HttpRequestBuilder.onBehalfOf(userToken: DecodedJWT) = onBehalfOf(Token(userToken))

internal data object AsApplication

/**
 * Gjør request som applikasjon, uansett kontekst.
 *
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [no.nav.hjelpemidler.http.context.RequestContext.principal]
 */
fun HttpRequestBuilder.asApplication() {
    attributes[AsApplicationKey] = AsApplication
}
