package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

internal fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    configuration: OpenIDClientConfiguration,
): OpenIDClient = when {
    configuration.cache -> CachedOpenIDClient(configuration, engine = engine)
    else -> DefaultOpenIDClient(configuration, engine = engine)
}

fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(
        engine = engine,
        configuration = OpenIDClientConfiguration().apply(block),
    )
