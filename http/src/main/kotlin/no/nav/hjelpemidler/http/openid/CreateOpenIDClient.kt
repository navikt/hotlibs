package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory
import no.nav.hjelpemidler.http.createHttpClientFactory

private val log = KotlinLogging.logger {}

internal fun createOpenIDClient(
    configuration: OpenIDClientConfiguration,
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
): OpenIDClient {
    log.info {
        "Lager OpenID-klient, tokenEndpoint: '${configuration.tokenEndpoint}', clientId: '${configuration.clientId}'"
    }

    return when {
        configuration.expiry === ExpireImmediately -> DefaultOpenIDClient(
            configuration = configuration,
            httpClientFactory = httpClientFactory,
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            httpClientFactory = httpClientFactory,
        )
    }
}

fun createOpenIDClient(
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient = createOpenIDClient(
    configuration = OpenIDClientConfiguration().apply(block),
    httpClientFactory = httpClientFactory,
)

fun createOpenIDClient(
    engine: HttpClientEngine,
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient = createOpenIDClient(
    configuration = OpenIDClientConfiguration().apply(block),
    httpClientFactory = createHttpClientFactory(engine),
)
