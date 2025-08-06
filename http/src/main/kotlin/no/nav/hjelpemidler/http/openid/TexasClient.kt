package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.ParametersBuilder
import io.ktor.http.parameters
import no.nav.hjelpemidler.configuration.TexasEnvironmentVariable
import no.nav.hjelpemidler.http.createHttpClient

private val log = KotlinLogging.logger {}

/**
 * @see <a href="https://docs.nais.io/auth/explanations/#texas">Texas</a>
 */
class TexasClient(
    engine: HttpClientEngine = CIO.create(),
    private val tokenUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_ENDPOINT,
    private val tokenExchangeUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_EXCHANGE_ENDPOINT,
    private val tokenIntrospectionUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_INTROSPECTION_ENDPOINT,
) {
    private val client: HttpClient = createHttpClient(engine) {
        expectSuccess = true
    }

    /**
     * Hent Machine-To-Machine-token (M2M-token) for [target].
     */
    suspend fun token(
        identityProvider: IdentityProvider,
        target: String,
        resource: String? = null,
        skipCache: Boolean? = null,
    ): TokenSet {
        log.debug { "token, url: '$tokenUrl', identityProvider: '$identityProvider', target: '$target', resource: '$resource', skipCache: $skipCache" }
        return execute(tokenUrl) {
            identityProvider(identityProvider)
            target(target)
            if (resource != null) resource(resource)
            if (skipCache != null) skipCache(skipCache)
        }
    }

    /**
     * Hent On-Behalf-Of-token (OBO-token) for [target] (token exchange).
     */
    suspend fun exchange(
        identityProvider: IdentityProvider,
        target: String,
        userToken: String,
        skipCache: Boolean? = null,
    ): TokenSet {
        log.debug { "exchange, url: '$tokenExchangeUrl', identityProvider: '$identityProvider', target: '$target', skipCache: $skipCache" }
        return execute(tokenExchangeUrl) {
            identityProvider(identityProvider)
            target(target)
            userToken(userToken)
            if (skipCache != null) skipCache(skipCache)
        }
    }

    /**
     * Hent On-Behalf-Of-token (OBO-token) for [target] (token exchange).
     */
    suspend fun exchange(
        identityProvider: IdentityProvider,
        target: String,
        userToken: DecodedJWT,
        skipCache: Boolean? = null,
    ): TokenSet = exchange(identityProvider, target, userToken.token, skipCache)

    /**
     * Valider [token].
     */
    suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection {
        log.debug { "introspection, url: '$tokenIntrospectionUrl', identityProvider: '$identityProvider'" }
        return execute(tokenIntrospectionUrl) {
            identityProvider(identityProvider)
            token(token)
        }
    }

    /**
     * Valider [token].
     */
    suspend fun introspection(identityProvider: IdentityProvider, token: DecodedJWT): TokenIntrospection =
        introspection(identityProvider, token.token)

    private suspend inline fun <reified T : Any> execute(
        url: String,
        noinline builder: ParametersBuilder.() -> Unit,
    ): T = client.submitForm(url = url, formParameters = parameters(builder)).body<T>()

    /**
     * Opprett [TokenSetProvider] for [identityProvider] som henter token eller gjør token exchange avhengig av
     * attributtene [userToken] og [tokenExchangePreventionToken]. `userToken` kan også defineres med [UserContext].
     *
     * @see [userToken]
     * @see [tokenExchangePreventionToken]
     * @see [TexasTokenSetProvider]
     */
    fun asTokenSetProvider(identityProvider: IdentityProvider, target: String): TokenSetProvider =
        TexasTokenSetProvider(this, identityProvider, target)

    fun asEntraIDTokenSetProvider(target: String): TokenSetProvider =
        TexasTokenSetProvider(this, IdentityProvider.ENTRA_ID, target)

    fun asIDPortenTokenSetProvider(target: String): TokenSetProvider =
        TexasTokenSetProvider(this, IdentityProvider.ID_PORTEN, target)

    fun asMaskinportenTokenSetProvider(target: String): TokenSetProvider =
        TexasTokenSetProvider(this, IdentityProvider.MASKINPORTEN, target)

    fun asTokenXTokenSetProvider(target: String): TokenSetProvider =
        TexasTokenSetProvider(this, IdentityProvider.TOKEN_X, target)

    /**
     * Opprett [OpenIDClient] for [identityProvider].
     */
    fun asOpenIDClient(identityProvider: IdentityProvider): OpenIDClient =
        TexasOpenIDClientAdapter(this, identityProvider)
}

private fun ParametersBuilder.identityProvider(value: IdentityProvider) = append("identity_provider", value.toString())
private fun ParametersBuilder.resource(value: String) = append("resource", value)
private fun ParametersBuilder.skipCache(value: Boolean) = append("skip_cache", value.toString())
private fun ParametersBuilder.target(value: String) = append("target", value)
private fun ParametersBuilder.token(value: String) = append("token", value)
private fun ParametersBuilder.userToken(value: String) = append("user_token", value)
