package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration

typealias OpenIDCacheConfigurer = CacheConfiguration<Parameters, TokenSet>.() -> Unit

val DEFAULT_OPENID_CACHE_CONFIGURER: OpenIDCacheConfigurer = {}

fun createOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    cacheConfigurer: OpenIDCacheConfigurer = DEFAULT_OPENID_CACHE_CONFIGURER,
): OpenIDClient =
    when (cacheConfigurer) {
        DEFAULT_OPENID_CACHE_CONFIGURER -> DefaultOpenIDClient(
            configuration = configuration,
            engine = engine
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            engine = engine,
            cacheConfigurer = cacheConfigurer,
        )
    }
