package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import kotlin.time.Duration.Companion.hours

internal class TexasStub(private val tokenSetProvider: TokenSetProvider) : Texas {
    override suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet =
        tokenSetProvider(request = HttpRequestBuilder())

    override suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet =
        TokenSet(accessToken = userToken, expiresIn = 1.hours)

    override suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection =
        TokenIntrospection(active = true)
}

fun TokenSetProvider.asTexas(): Texas = TexasStub(this)
fun TokenSet.asTexas(): Texas = TexasStub(asTokenSetProvider())
