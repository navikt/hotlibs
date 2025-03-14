package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.AsyncCache
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import no.nav.hjelpemidler.cache.createCache
import no.nav.hjelpemidler.cache.getAsync
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

private val log = KotlinLogging.logger {}

internal class CachedOpenIDClient(
    configuration: OpenIDClientConfiguration,
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
) : OpenIDClient {
    private val wrapped: OpenIDClient = DefaultOpenIDClient(
        configuration = configuration,
        httpClientFactory = httpClientFactory,
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
            wrapped.grant(builder)
        }
    }
}
