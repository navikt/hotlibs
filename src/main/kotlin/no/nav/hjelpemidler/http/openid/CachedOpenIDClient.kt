package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import no.nav.hjelpemidler.cache.CacheConfigurer
import no.nav.hjelpemidler.cache.createAsyncCache
import no.nav.hjelpemidler.cache.getAsync

internal class CachedOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    cacheConfiguration: CacheConfigurer<Parameters, TokenSet> = {
        tokenExpiry()
    },
) : OpenIDClient {
    private val client: OpenIDClient = DefaultOpenIDClient(configuration = configuration, engine = engine)
    private val cache: AsyncCache<Parameters, TokenSet> = createAsyncCache(configuration = cacheConfiguration)

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet =
        cache.getAsync(Parameters.build(builder)) { _ ->
            client.grant(builder)
        }
}
