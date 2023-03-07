package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import no.nav.hjelpemidler.configuration.EnvironmentVariable

object TokenXEnvironmentVariable {
    val TOKEN_X_CLIENT_ID by EnvironmentVariable
    val TOKEN_X_ISSUER by EnvironmentVariable
    val TOKEN_X_JWKS_URI by EnvironmentVariable
    val TOKEN_X_PRIVATE_JWK by EnvironmentVariable
    val TOKEN_X_TOKEN_ENDPOINT by EnvironmentVariable
    val TOKEN_X_WELL_KNOWN_URL by EnvironmentVariable
}

fun tokenXEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID,
    clientSecret = null,
)

fun tokenXClient(
    configuration: OpenIDConfiguration = tokenXEnvironmentConfiguration(),
    engine: HttpClientEngine = CIO.create(),
    expiry: Expiry<Parameters, TokenSet>? = null,
    cacheConfigurer: CacheConfiguration.() -> Unit = DEFAULT_CACHE_CONFIGURER,
): OpenIDClient =
    createOpenIDClient(
        configuration = configuration,
        engine = engine,
        expiry = expiry,
        cacheConfigurer = cacheConfigurer,
    )
