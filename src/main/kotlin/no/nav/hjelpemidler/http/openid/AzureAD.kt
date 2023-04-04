package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.configuration.External

object AzureADEnvironmentVariable {
    @External
    val AZURE_APP_CLIENT_ID by EnvironmentVariable

    @External
    val AZURE_APP_CLIENT_SECRET by EnvironmentVariable

    @External
    val AZURE_APP_JWK by EnvironmentVariable

    @External
    val AZURE_APP_JWKS by EnvironmentVariable

    @External
    val AZURE_APP_PRE_AUTHORIZED_APPS by EnvironmentVariable

    @External
    val AZURE_APP_TENANT_ID by EnvironmentVariable

    @External
    val AZURE_APP_WELL_KNOWN_URL by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_ISSUER by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_JWKS_URI by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_TOKEN_ENDPOINT by EnvironmentVariable
}

fun azureADEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = AzureADEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = AzureADEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = AzureADEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun azureADClient(
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
): OpenIDClient =
    createOpenIDClient(engine = engine) {
        azureADEnvironmentConfiguration()
        block()
    }
