package no.nav.hjelpemidler.http.openid

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
     * Valider [token].
     */
    suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection {
        log.debug { "introspection, url: '$tokenIntrospectionUrl', identityProvider: '$identityProvider'" }
        return execute(tokenIntrospectionUrl) {
            identityProvider(identityProvider)
            token(token)
        }
    }

    private suspend inline fun <reified T : Any> execute(
        url: String,
        noinline builder: ParametersBuilder.() -> Unit,
    ): T = client.submitForm(url = url, formParameters = parameters(builder)).body<T>()

    /**
     * [TokenSetProvider] for [IdentityProvider.ENTRA_ID].
     *
     * @see [DelegatingTokenSetProvider]
     */
    fun entraId(defaultTarget: String): TokenSetProvider =
        DelegatingTokenSetProvider(this, IdentityProvider.ENTRA_ID, defaultTarget)

    /**
     * [TokenSetProvider] for [IdentityProvider.ENTRA_ID].
     *
     * @see [ApplicationTokenSetProvider]
     */
    fun entraIdApplication(defaultTarget: String): TokenSetProvider =
        ApplicationTokenSetProvider(this, IdentityProvider.ENTRA_ID, defaultTarget)

    /**
     * [TokenSetProvider] for [IdentityProvider.ENTRA_ID].
     *
     * @see [UserTokenSetProvider]
     */
    fun entraIdUser(defaultTarget: String): TokenSetProvider =
        UserTokenSetProvider(this, IdentityProvider.ENTRA_ID, defaultTarget)

    /**
     * [TokenSetProvider] for [IdentityProvider.MASKINPORTEN].
     *
     * @see [ApplicationTokenSetProvider]
     */
    fun maskinporten(defaultTarget: String): TokenSetProvider =
        ApplicationTokenSetProvider(this, IdentityProvider.MASKINPORTEN, defaultTarget)

    /**
     * [TokenSetProvider] for [IdentityProvider.TOKEN_X].
     *
     * @see [UserTokenSetProvider]
     */
    fun tokenX(defaultTarget: String): TokenSetProvider =
        UserTokenSetProvider(this, IdentityProvider.TOKEN_X, defaultTarget)

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
