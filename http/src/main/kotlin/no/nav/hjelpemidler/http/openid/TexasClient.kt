package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.forms.submitForm
import io.ktor.http.ParametersBuilder
import io.ktor.http.parameters
import no.nav.hjelpemidler.configuration.TexasEnvironmentVariable
import no.nav.hjelpemidler.http.createHttpClient
import java.io.Closeable

private val log = KotlinLogging.logger {}

/**
 * @see <a href="https://docs.nais.io/auth/explanations/#texas">Texas</a>
 */
class TexasClient(
    engine: HttpClientEngine,
    private val tokenUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_ENDPOINT,
    private val tokenExchangeUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_EXCHANGE_ENDPOINT,
    private val tokenIntrospectionUrl: String = TexasEnvironmentVariable.NAIS_TOKEN_INTROSPECTION_ENDPOINT,
) : Texas, Closeable {
    private val client: HttpClient = createHttpClient(engine) {
        expectSuccess = true
    }

    /**
     * Hent Machine-To-Machine-token (M2M-token) for [target].
     */
    override suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet {
        log.debug { "Henter token, url: '$tokenUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(tokenUrl) {
            identityProvider(identityProvider)
            target(target)
        }
    }

    /**
     * Hent On-Behalf-Of-token (OBO-token) for [target] (token exchange).
     */
    override suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet {
        log.debug { "Gjør token exchange, url: '$tokenExchangeUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(tokenExchangeUrl) {
            identityProvider(identityProvider)
            target(target)
            userToken(userToken)
        }
    }

    /**
     * Valider [token].
     */
    override suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection {
        log.debug { "Gjør introspection, url: '$tokenIntrospectionUrl', identityProvider: '$identityProvider'" }
        return execute(tokenIntrospectionUrl) {
            identityProvider(identityProvider)
            token(token)
        }
    }

    private suspend inline fun <reified T : Any> execute(
        url: String,
        noinline builder: ParametersBuilder.() -> Unit,
    ): T = client.submitForm(url = url, formParameters = parameters(builder)).body<T>()

    override fun close() = client.close()
}

private fun ParametersBuilder.identityProvider(value: IdentityProvider) = append("identity_provider", value.toString())
private fun ParametersBuilder.target(value: String) = append("target", value)
private fun ParametersBuilder.token(value: String) = append("token", value)
private fun ParametersBuilder.userToken(value: String) = append("user_token", value)
