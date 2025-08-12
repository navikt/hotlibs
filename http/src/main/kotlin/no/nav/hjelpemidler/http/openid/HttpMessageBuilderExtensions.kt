package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) = bearerAuth(tokenSet.accessToken)

/**
 * Sett `target` for request.
 */
fun HttpRequestBuilder.target(target: String) {
    attributes[TargetKey] = TargetValue(target)
}

internal data object AsApplicationValue

/**
 * Gjør request som applikasjon, uansett kontekst.
 *
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [OpenIDContext]
 */
fun HttpRequestBuilder.asApplication() {
    attributes[AsApplicationKey] = AsApplicationValue
}

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.asApplication]
 */
fun HttpRequestBuilder.onBehalfOf(userToken: String) {
    attributes[UserTokenKey] = UserTokenValue(userToken)
}

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.asApplication]
 */
fun HttpRequestBuilder.onBehalfOf(userToken: DecodedJWT) = onBehalfOf(userToken.token)

@JvmInline
internal value class TargetValue(private val value: String) {
    override fun toString(): String = value
}

@JvmInline
internal value class UserTokenValue(private val value: String) {
    override fun toString(): String = value
}

internal val TargetKey = AttributeKey<TargetValue>("Target")
internal val AsApplicationKey = AttributeKey<AsApplicationValue>("AsApplication")
internal val UserTokenKey = AttributeKey<UserTokenValue>("UserToken")
