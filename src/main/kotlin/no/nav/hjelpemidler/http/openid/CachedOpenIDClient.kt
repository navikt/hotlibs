package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import no.nav.hjelpemidler.cache.configure
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.cache.getAsync

internal class CachedOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    cacheConfigurer: OpenIDCacheConfigurer = {
        tokenExpiry()
    },
) : OpenIDClient {
    private val client: OpenIDClient = DefaultOpenIDClient(
        configuration = configuration,
        engine = engine,
    )
    private val cache: AsyncCache<Parameters, TokenSet> = createCache(cacheConfigurer)
        .buildAsync()

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet =
        cache.getAsync(Parameters.build(builder)) { _ ->
            client.grant(builder)
        }
}
