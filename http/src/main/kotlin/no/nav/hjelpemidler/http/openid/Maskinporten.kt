package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

private val log = KotlinLogging.logger {}

fun maskinportenEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = MaskinportenEnvironmentVariable.MASKINPORTEN_TOKEN_ENDPOINT,
    // Merk: Disse er ikke relevante for maskinporten backend'en, men kreves av hotlibs-http sin openid client
    clientId = MaskinportenEnvironmentVariable.MASKINPORTEN_CLIENT_ID,
    clientSecret = "<n/a>",
)

fun maskinportenClient(
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(httpClientFactory = httpClientFactory) {
        maskinportenEnvironmentConfiguration()
        block()
    }

fun maskinportenClient(
    engine: HttpClientEngine,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        maskinportenEnvironmentConfiguration()
        block()
    }
