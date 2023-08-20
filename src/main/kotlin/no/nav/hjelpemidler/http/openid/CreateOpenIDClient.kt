package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

internal fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    configuration: OpenIDClientConfiguration,
): OpenIDClient {
    log.info {
        "Lager OpenID-klient, tokenEndpoint: '${configuration.tokenEndpoint}', clientId: '${configuration.clientId}'"
    }

    return when {
        configuration.expiry === ExpireImmediately -> DefaultOpenIDClient(
            configuration = configuration,
            engine = engine,
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            engine = engine,
        )
    }
}

fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient = createOpenIDClient(
    engine = engine,
    configuration = OpenIDClientConfiguration().apply(block),
)
