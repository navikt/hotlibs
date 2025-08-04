package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import io.ktor.util.appendAll
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
     * Utsted Machine-To-Machine-token (M2M-token) for [target].
     */
    suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet {
        val url = tokenUrl
        log.debug { "token, url: '$url', identityProvider: '$identityProvider', target: '$target'" }
        return execute(
            url = url,
            identityProvider = identityProvider,
            parameters = mapOf(
                // "resource" to "",
                // "skip_cache" to "true",
                "target" to target,
            ),
        )
    }

    /**
     * Utsted On-Behalf-Of-token (OBO-token) for [target].
     */
    suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet {
        val url = tokenExchangeUrl
        log.debug { "exchange, url: '$url', identityProvider: '$identityProvider', target: '$target'" }
        return execute(
            url = url,
            identityProvider = identityProvider,
            parameters = mapOf(
                // "skip_cache" to "true",
                "target" to target,
                "user_token" to userToken,
            ),
        )
    }

    /**
     * Valider [token].
     */
    suspend fun introspection(identityProvider: IdentityProvider, token: String): TokenIntrospection {
        val url = tokenIntrospectionUrl
        log.debug { "introspection, url: '$url', identityProvider: '$identityProvider'" }
        return execute(
            url = url,
            identityProvider = identityProvider,
            parameters = mapOf(
                "token" to token,
            )
        )
    }

    fun asOpenIDClient(identityProvider: IdentityProvider): OpenIDClient =
        TexasOpenIDClient(this, identityProvider)

    fun asTokenSetProvider(identityProvider: IdentityProvider, target: String): TokenSetProvider =
        TokenSetProvider { token(identityProvider, target) }

    private suspend inline fun <reified T : Any> execute(
        url: String,
        identityProvider: IdentityProvider,
        parameters: Map<String, String>,
    ): T = client
        .submitForm(url = url, formParameters = parameters {
            append("identity_provider", identityProvider.toString())
            appendAll(parameters)
        })
        .body<T>()
}

/*
@Suppress("PropertyName")
class TexasRequest(
    private val parameters: MutableMap<String, String> = mutableMapOf(),
) : Map<String, String> by parameters {
    var identity_provider by parameters
    var resource by parameters
    var skip_cache by parameters
    var target by parameters
    var token by parameters
    var user_token by parameters
}

fun texasRequestOf(block: TexasRequest.() -> Unit): TexasRequest {
    return TexasRequest().apply(block)
}
*/
