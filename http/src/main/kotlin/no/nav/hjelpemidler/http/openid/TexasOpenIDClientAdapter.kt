package no.nav.hjelpemidler.http.openid

/**
 * Tilby [TexasClient] som [OpenIDClient] for [identityProvider].
 */
internal class TexasOpenIDClientAdapter(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
) : OpenIDClient {
    override suspend fun grant(scope: String): TokenSet =
        client.token(identityProvider, target = scope)

    override suspend fun grant(scope: String, onBehalfOf: String): TokenSet =
        client.exchange(identityProvider, target = scope, userToken = onBehalfOf)
}
