package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.parameters
import no.nav.hjelpemidler.cache.CoroutinesCache
import no.nav.hjelpemidler.cache.NoOpCoroutinesCache
import no.nav.hjelpemidler.cache.coroutines
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.http.createHttpClient

private val log = KotlinLogging.logger {}

internal open class DefaultOpenIDClient(
    private val configuration: OpenIDClientConfiguration,
    engine: HttpClientEngine = CIO.create(),
) : OpenIDClient {
    private val client: HttpClient = createHttpClient(engine) {
        expectSuccess = false
    }

    private val cache: CoroutinesCache<Parameters, TokenSet> = if (configuration.expiry == ExpireImmediately) {
        NoOpCoroutinesCache()
    } else {
        createCache(configuration.cacheConfiguration)
            .expireAfter(configuration.expiry)
            .removalListener<Parameters, TokenSet> { parameters, tokenSet, cause ->
                log.debug {
                    "TokenSet ble fjernet fra cache, scope: ${parameters?.scope}, expiresAt: ${tokenSet?.expiresAt}, cause: $cause"
                }
            }
            .buildAsync<Parameters, TokenSet>()
            .coroutines()
    }

    override suspend fun grant(scope: String): TokenSet = execute {
        grantType(GrantType.CLIENT_CREDENTIALS)
        scope(scope)
    }

    override suspend fun grant(scope: String, onBehalfOf: String): TokenSet = execute {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf)
        requestedTokenUse("on_behalf_of")
    }

    private suspend fun execute(block: ParametersBuilder.() -> Unit): TokenSet {
        val formParameters = parameters {
            clientId(configuration.clientId)
            clientSecret(checkNotNull(configuration.clientSecret) {
                "Mangler verdi for 'clientSecret'"
            })
            block()
        }
        log.debug { "Henter token, scope: '${formParameters.scope}', grantType: '${formParameters.grantType}'" }
        return cache.get(formParameters) {
            log.debug { "Cache miss, henter nytt token, scope: '${formParameters.scope}'" }
            val response = client.submitForm(url = configuration.tokenEndpoint, formParameters = it)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenSet = response.body<TokenSet>()
                    log.debug { "Hentet token, expiresAt: ${tokenSet.expiresAt}" }
                    tokenSet
                }

                HttpStatusCode.BadRequest -> openIDError(it, "Ugyldig forespørsel", response)
                else -> openIDError(it, "Noe gikk galt", response)
            }
        }
    }
}

private fun ParametersBuilder.assertion(value: String) = append("assertion", value)
private fun ParametersBuilder.clientId(value: String) = append("client_id", value)
private fun ParametersBuilder.clientSecret(value: String) = append("client_secret", value)
private fun ParametersBuilder.grantType(value: String) = append("grant_type", value)
private fun ParametersBuilder.requestedTokenUse(value: String) = append("requested_token_use", value)
private fun ParametersBuilder.scope(value: String) = append("scope", value)
