package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration

internal val DEFAULT_CACHE_CONFIGURER: CacheConfiguration.() -> Unit = {}

fun createOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    expiry: Expiry<Parameters, TokenSet>? = null,
    cacheConfigurer: CacheConfiguration.() -> Unit = DEFAULT_CACHE_CONFIGURER,
): OpenIDClient =
    when (expiry) {
        null -> DefaultOpenIDClient(
            configuration = configuration,
            engine = engine
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            engine = engine,
            expiry = expiry,
            cacheConfigurer = cacheConfigurer,
        )
    }
