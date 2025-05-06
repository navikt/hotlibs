package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.configuration.EntraIDEnvironmentVariable
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

fun entraIDEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = EntraIDEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun entraIDClient(
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(httpClientFactory = httpClientFactory) {
        entraIDEnvironmentConfiguration()
        block()
    }

fun entraIDClient(
    engine: HttpClientEngine,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        entraIDEnvironmentConfiguration()
        block()
    }
