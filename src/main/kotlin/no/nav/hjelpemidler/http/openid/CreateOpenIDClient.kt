package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import mu.KotlinLogging

internal val EXPIRE_IMMEDIATELY: Expiry<Parameters, TokenSet> = object : Expiry<Parameters, TokenSet> {
    override fun expireAfterCreate(key: Parameters?, value: TokenSet?, currentTime: Long): Long =
        0

    override fun expireAfterUpdate(key: Parameters?, value: TokenSet?, currentTime: Long, currentDuration: Long): Long =
        0

    override fun expireAfterRead(key: Parameters?, value: TokenSet?, currentTime: Long, currentDuration: Long): Long =
        0
}

private val log = KotlinLogging.logger {}

internal fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    configuration: OpenIDClientConfiguration,
): OpenIDClient {
    log.info {
        "Lager OpenID-klient, tokenEndpoint: '${configuration.tokenEndpoint}', clientId: '${configuration.clientId}'"
    }

    return when {
        configuration.expiry === EXPIRE_IMMEDIATELY -> DefaultOpenIDClient(
            configuration = configuration,
            engine = engine,
        )

        else -> CachedOpenIDClient(
            configuration = configuration,
            engine = engine,
        )
    }
}

fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(
        engine = engine,
        configuration = OpenIDClientConfiguration().apply(block),
    )
