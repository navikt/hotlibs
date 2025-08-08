package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) = bearerAuth(tokenSet.accessToken)

internal val TargetKey = AttributeKey<Target>("Target")
internal val UserTokenKey = AttributeKey<Token>("UserToken")
internal val SystembrukerTokenKey = AttributeKey<SystembrukerToken>("SystembrukerToken")

/**
 * Sett `target` for request.
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.target(target: Target) {
    attributes[TargetKey] = target
}

/**
 * Sett `target` for request.
 *
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.target(target: String) = target(Target(target))

/**
 * Sett `userToken` for request.
 *
 * @see [HttpRequestBuilder.systembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: Token) {
    attributes[UserTokenKey] = userToken
}

/**
 * Sett `userToken` for request.
 *
 * @see [HttpRequestBuilder.systembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: String) = påVegneAv(Token(userToken))

/**
 * Sett `userToken` for request.
 *
 * @see [HttpRequestBuilder.systembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: DecodedJWT) = påVegneAv(Token(userToken))

internal data object SystembrukerToken

/**
 * Gjør request som systembruker, uansett brukerkontekst.
 *
 * @see [UserContext]
 * @see [HttpRequestBuilder.påVegneAv]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.systembruker() {
    attributes[SystembrukerTokenKey] = SystembrukerToken
}
