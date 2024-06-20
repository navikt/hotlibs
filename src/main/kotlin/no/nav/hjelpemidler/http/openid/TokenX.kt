package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.TokenX

fun tokenXEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = TokenX.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenX.TOKEN_X_CLIENT_ID,
    clientSecret = null,
)

fun tokenXClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        tokenXEnvironmentConfiguration()
        block()
    }
