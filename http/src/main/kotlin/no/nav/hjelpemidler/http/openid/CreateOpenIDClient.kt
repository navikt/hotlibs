package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

private val log = KotlinLogging.logger {}

internal fun createOpenIDClient(
    configuration: OpenIDClientConfiguration,
    engine: HttpClientEngine = CIO.create(),
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
    configuration = OpenIDClientConfiguration().apply(block),
    engine = engine,
)
