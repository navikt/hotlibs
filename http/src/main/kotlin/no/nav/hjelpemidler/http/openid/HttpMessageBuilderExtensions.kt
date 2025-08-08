package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.util.AttributeKey

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet) = bearerAuth(tokenSet.accessToken)

internal val TargetKey = AttributeKey<Target>("Target")
internal val UserTokenKey = AttributeKey<Token>("UserToken")
internal val SomSystembrukerKey = AttributeKey<SomSystembruker>("SomSystembruker")

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
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.somSystembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: Token) {
    attributes[UserTokenKey] = userToken
}

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.somSystembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: String) = påVegneAv(Token(userToken))

/**
 * Gjør request på vegne av bruker.
 *
 * @see [HttpRequestBuilder.somSystembruker]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.påVegneAv(userToken: DecodedJWT) = påVegneAv(Token(userToken))

internal data object SomSystembruker

/**
 * Gjør request som systembruker, uansett brukerkontekst.
 *
 * @see [UserContext]
 * @see [HttpRequestBuilder.påVegneAv]
 * @see [TexasTokenSetProvider]
 */
fun HttpRequestBuilder.somSystembruker() {
    attributes[SomSystembrukerKey] = SomSystembruker
}
