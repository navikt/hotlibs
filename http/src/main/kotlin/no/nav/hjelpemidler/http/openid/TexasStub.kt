package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

class TexasStub(private val tokenSetProvider: TokenSetProvider) : Texas {
    constructor(tokenSet: TokenSet) : this(tokenSet.asTokenSetProvider())

    override suspend fun token(
        identityProvider: IdentityProvider,
        target: String,
        resource: String?,
        skipCache: Boolean?,
    ): TokenSet = tokenSetProvider(HttpRequestBuilder())

    override suspend fun exchange(
        identityProvider: IdentityProvider,
        target: String,
        userToken: String,
        skipCache: Boolean?,
    ): TokenSet = tokenSetProvider(HttpRequestBuilder())

    override suspend fun introspection(
        identityProvider: IdentityProvider,
        token: String,
    ): TokenIntrospection = TokenIntrospection(active = true)
}

fun TokenSetProvider.asTexas(): Texas = TexasStub(this)
