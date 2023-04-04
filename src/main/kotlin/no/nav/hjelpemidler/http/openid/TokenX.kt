package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.configuration.External

object TokenXEnvironmentVariable {
    @External
    val TOKEN_X_CLIENT_ID by EnvironmentVariable

    @External
    val TOKEN_X_ISSUER by EnvironmentVariable

    @External
    val TOKEN_X_JWKS_URI by EnvironmentVariable

    @External
    val TOKEN_X_PRIVATE_JWK by EnvironmentVariable

    @External
    val TOKEN_X_TOKEN_ENDPOINT by EnvironmentVariable

    @External
    val TOKEN_X_WELL_KNOWN_URL by EnvironmentVariable
}

fun tokenXEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID,
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
