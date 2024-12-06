package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

fun tokenXEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID,
    clientSecret = null,
)

fun tokenXClient(
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(httpClientFactory = httpClientFactory) {
        tokenXEnvironmentConfiguration()
        block()
    }

fun tokenXClient(
    engine: HttpClientEngine,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        tokenXEnvironmentConfiguration()
        block()
    }
