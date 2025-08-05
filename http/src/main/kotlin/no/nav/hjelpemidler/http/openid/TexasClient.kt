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
    suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet {
        log.debug { "token, url: '$tokenUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(tokenUrl) {
            // resource("")
            // skipCache(true)
            target(target)
        }
    }

    /**
     * Hent On-Behalf-Of-token (OBO-token) for [target].
     */
    suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet {
        log.debug { "exchange, url: '$tokenExchangeUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(tokenExchangeUrl) {
            // skipCache(true)
            target(target)
            userToken(userToken)
        }
    }

    /**
     * Valider [token].
     */
    suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection {
        log.debug { "introspection, url: '$tokenIntrospectionUrl', identityProvider: '$identityProvider'" }
        return execute(tokenIntrospectionUrl) {
            token(token)
        }
    }

    private suspend inline fun <reified T : Any> execute(
        url: String,
        noinline builder: ParametersBuilder.() -> Unit,
    ): T = client
        .submitForm(url = url, formParameters = parameters(builder))
        .body<T>()

    fun asOpenIDClient(identityProvider: IdentityProvider): OpenIDClient =
        TexasOpenIDClient(this, identityProvider)

    fun asTokenSetProvider(identityProvider: IdentityProvider, target: String): TokenSetProvider =
        TokenSetProvider {
            val userToken = it.userToken()
            if (userToken != null && it.tokenExchangePreventionToken() == null) {
                exchange(identityProvider, target, userToken)
            } else {
                token(identityProvider, target)
            }
        }
}

private fun ParametersBuilder.identityProvider(value: IdentityProvider) = append("identity_provider", value.toString())
private fun ParametersBuilder.resource(value: String) = append("resource", value)
private fun ParametersBuilder.skipCache(value: Boolean) = append("skip_cache", value.toString())
private fun ParametersBuilder.target(value: String) = append("target", value)
private fun ParametersBuilder.token(value: String) = append("token", value)
private fun ParametersBuilder.userToken(value: String) = append("user_token", value)
