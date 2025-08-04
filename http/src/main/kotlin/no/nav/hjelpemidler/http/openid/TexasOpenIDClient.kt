package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

class TexasOpenIDClient(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
) : OpenIDClient {
    constructor(identityProvider: IdentityProvider, engine: HttpClientEngine = CIO.create()) : this(
        client = TexasClient(engine),
        identityProvider = identityProvider,
    )

    override suspend fun grant(scope: String): TokenSet =
        client.token(identityProvider, target = scope)

    override suspend fun grant(scope: String, onBehalfOf: String): TokenSet =
        client.exchange(identityProvider, target = scope, userToken = onBehalfOf)

    override fun withMaskinportenAssertion(scope: String): TokenSetProvider {
        check(identityProvider == IdentityProvider.MASKINPORTEN) {
            "identityProvider var $identityProvider, forventet: ${IdentityProvider.MASKINPORTEN}"
        }
        return TokenSetProvider {
            client.token(identityProvider, target = scope)
        }
    }
}
