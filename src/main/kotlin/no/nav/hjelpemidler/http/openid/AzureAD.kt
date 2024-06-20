package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.EntraID

fun azureADEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = EntraID.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = EntraID.AZURE_APP_CLIENT_ID,
    clientSecret = EntraID.AZURE_APP_CLIENT_SECRET,
)

fun azureADClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        azureADEnvironmentConfiguration()
        block()
    }
