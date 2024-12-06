package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.configuration.EntraIDEnvironmentVariable
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

fun azureADEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = EntraIDEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun azureADClient(
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(httpClientFactory = httpClientFactory) {
        azureADEnvironmentConfiguration()
        block()
    }

fun azureADClient(
    engine: HttpClientEngine,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        azureADEnvironmentConfiguration()
        block()
    }
