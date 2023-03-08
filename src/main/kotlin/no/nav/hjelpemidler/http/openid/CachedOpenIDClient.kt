package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import mu.KotlinLogging
import no.nav.hjelpemidler.cache.CacheConfiguration
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.cache.getAsync
import org.slf4j.LoggerFactory

private val log = KotlinLogging.logger {}

internal class CachedOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    expiry: Expiry<Parameters, TokenSet> = TokenExpiry(),
    cacheConfigurer: CacheConfiguration.() -> Unit = {
    },
) : OpenIDClient {
    private val client: OpenIDClient = DefaultOpenIDClient(
        configuration = configuration,
        engine = engine,
    )
    private val cache: AsyncCache<Parameters, TokenSet> = createCache(cacheConfigurer)
        .expireAfter(expiry)
        .buildAsync()

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet =
        cache.getAsync(Parameters.build(builder)) { _ ->
            client.grant(builder)
        }
}
