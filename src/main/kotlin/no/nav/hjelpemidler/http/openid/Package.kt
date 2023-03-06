package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfigurer
import kotlin.time.Duration

fun createOpenIDClient(
    configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
    cacheConfiguration: CacheConfigurer<Parameters, TokenSet>? = null,
): OpenIDClient =
    when (cacheConfiguration) {
        null -> DefaultOpenIDClient(
            configuration = configuration,
            engine = engine
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            engine = engine,
            cacheConfiguration = cacheConfiguration,
        )
    }

fun Caffeine<Parameters, TokenSet>.tokenExpiry(leeway: Duration = TokenExpiry.LEEWAY): Caffeine<Parameters, TokenSet> =
    expireAfter(TokenExpiry(leeway = leeway))
