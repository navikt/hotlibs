package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.EntraIDEnvironmentVariable

fun entraIDEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = EntraIDEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun entraIDClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine) {
        entraIDEnvironmentConfiguration()
        block()
    }
