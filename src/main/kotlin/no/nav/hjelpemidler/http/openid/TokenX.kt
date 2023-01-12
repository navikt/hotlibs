package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

object TokenXEnvironmentVariable {
    val TOKEN_X_WELL_KNOWN_URL by EnvironmentVariable
    val TOKEN_X_CLIENT_ID by EnvironmentVariable
    val TOKEN_X_PRIVATE_JWK by EnvironmentVariable
    val TOKEN_X_ISSUER by EnvironmentVariable
    val TOKEN_X_JWKS_URI by EnvironmentVariable
    val TOKEN_X_TOKEN_ENDPOINT by EnvironmentVariable
}

fun tokenXEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID,
    clientSecret = null,
)

fun tokenXClient(
    configuration: OpenIDConfiguration = tokenXEnvironmentConfiguration(),
    engine: HttpClientEngine = CIO.create(),
): OpenIDClient = DefaultOpenIDClient(configuration = configuration, engine = engine)
