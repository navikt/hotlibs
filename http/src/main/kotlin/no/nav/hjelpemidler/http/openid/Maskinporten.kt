package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable

private val log = KotlinLogging.logger {}

fun maskinportenEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = MaskinportenEnvironmentVariable.MASKINPORTEN_TOKEN_ENDPOINT,
    // NB! Disse er ikke relevante for Maskinporten, men kreves av [no.nav.hjelpemidler.http.openid.OpenIDClient].
    clientId = MaskinportenEnvironmentVariable.MASKINPORTEN_CLIENT_ID,
    clientSecret = "",
)

fun maskinportenClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine) {
        maskinportenEnvironmentConfiguration()
        block()
    }
