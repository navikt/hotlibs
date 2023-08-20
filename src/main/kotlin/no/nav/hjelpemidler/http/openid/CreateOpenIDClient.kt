package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Parameters
import no.nav.hjelpemidler.configuration.Environment
import kotlin.time.Duration.Companion.seconds

internal val EXPIRE_IMMEDIATELY: Expiry<Parameters, TokenSet> = TokenExpiry(0.seconds)

internal fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    configuration: OpenIDClientConfiguration,
): OpenIDClient = when {
    configuration.expiry === EXPIRE_IMMEDIATELY -> DefaultOpenIDClient(
        configuration = configuration,
        engine = engine,
    )

    else -> CachedOpenIDClient(
        configuration = configuration,
        engine = engine,
    )
}

fun createOpenIDClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(
        engine = engine,
        configuration = OpenIDClientConfiguration().apply(block),
    )

fun scopeOf(application: String, namespace: String = "teamdigihot"): String {
    val cluster = Environment.current
    return "api://$cluster.$namespace.$application/.default"
}
