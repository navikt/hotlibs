package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.ParametersBuilder
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

    fun asOpenIDClient(identityProvider: IdentityProvider): OpenIDClient =
        TexasOpenIDClient(this, identityProvider)

    fun asTokenSetProvider(identityProvider: IdentityProvider, target: String): TokenSetProvider =
        TokenSetProvider { token(identityProvider, target) }

    /**
     * M2M-token for [target].
     */
    suspend fun token(identityProvider: IdentityProvider, target: String): TokenSet {
        log.debug { "token, url: '$tokenUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(
            url = tokenUrl,
            identityProvider = identityProvider,
            parameters = mapOf(
                // "resource" to "",
                // "skip_cache" to "true",
                "target" to target,
            ),
        )
    }

    /**
     * OBO-token for [target].
     */
    suspend fun exchange(identityProvider: IdentityProvider, target: String, userToken: String): TokenSet {
        log.debug { "exchange, url: '$tokenExchangeUrl', identityProvider: '$identityProvider', target: '$target'" }
        return execute(
            url = tokenExchangeUrl,
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
        log.debug { "introspection, url: '$tokenIntrospectionUrl', identityProvider: '$identityProvider'" }
        return execute(
            url = tokenIntrospectionUrl,
            identityProvider = identityProvider,
            parameters = mapOf(
                "token" to token,
            )
        )
    }

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

class TexasOpenIDClient(
    private val client: TexasClient,
    private val identityProvider: IdentityProvider,
) : OpenIDClient {
    constructor(identityProvider: IdentityProvider, engine: HttpClientEngine = CIO.create()) : this(
        client = TexasClient(engine),
        identityProvider = identityProvider,
    )

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet {
        TODO("Not yet implemented")
    }

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
