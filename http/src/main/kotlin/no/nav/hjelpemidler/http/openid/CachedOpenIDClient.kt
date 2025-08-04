package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.cache.getAsync

private val log = KotlinLogging.logger {}

internal class CachedOpenIDClient(
    configuration: OpenIDClientConfiguration,
    engine: HttpClientEngine = CIO.create(),
) : DefaultOpenIDClient(configuration, engine) {
    private val cache: AsyncCache<Parameters, TokenSet> = createCache(configuration.cacheConfiguration)
        .expireAfter(configuration.expiry)
        .removalListener<Parameters, TokenSet> { parameters, tokenSet, cause ->
            withLoggingContext(
                "scope" to parameters?.scope,
                "expiresAt" to tokenSet?.expiresAt?.toString(),
                "cause" to cause.toString(),
            ) {
                log.debug {
                    "TokenSet ble fjernet fra cache"
                }
            }
        }
        .buildAsync()

    override suspend fun execute(formParameters: Parameters): TokenSet =
        cache.getAsync(formParameters) {
            log.debug { "Cache miss, henter nytt token, scope: '${formParameters.scope}'" }
            super.execute(it)
        }
}
