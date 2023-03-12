package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import mu.KotlinLogging
import mu.withLoggingContext
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.cache.getAsync

private val log = KotlinLogging.logger {}

internal class CachedOpenIDClient(
    configuration: OpenIDClientConfiguration,
    engine: HttpClientEngine = CIO.create(),
) : OpenIDClient {
    private val client: OpenIDClient = DefaultOpenIDClient(
        configuration = configuration,
        engine = engine,
    )
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

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet {
        val formParameters = Parameters.build(builder)
        return cache.getAsync(formParameters) { _ ->
            log.debug { "Cache miss, henter nytt token, scope: '${formParameters.scope}'" }
            client.grant(builder)
        }
    }
}
