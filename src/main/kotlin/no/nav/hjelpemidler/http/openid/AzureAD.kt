package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.EnvironmentVariable

object AzureADEnvironmentVariable {
    val AZURE_APP_CLIENT_ID by EnvironmentVariable
    val AZURE_APP_CLIENT_SECRET by EnvironmentVariable
    val AZURE_APP_JWK by EnvironmentVariable
    val AZURE_APP_JWKS by EnvironmentVariable
    val AZURE_APP_PRE_AUTHORIZED_APPS by EnvironmentVariable
    val AZURE_APP_TENANT_ID by EnvironmentVariable
    val AZURE_APP_WELL_KNOWN_URL by EnvironmentVariable
    val AZURE_OPENID_CONFIG_ISSUER by EnvironmentVariable
    val AZURE_OPENID_CONFIG_JWKS_URI by EnvironmentVariable
    val AZURE_OPENID_CONFIG_TOKEN_ENDPOINT by EnvironmentVariable
}

fun azureADEnvironmentConfiguration(): OpenIDConfiguration = DefaultOpenIDConfiguration(
    tokenEndpoint = AzureADEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = AzureADEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = AzureADEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun azureADClient(
    configuration: OpenIDConfiguration = azureADEnvironmentConfiguration(),
    engine: HttpClientEngine = CIO.create(),
): OpenIDClient = DefaultOpenIDClient(configuration = configuration, engine = engine)
