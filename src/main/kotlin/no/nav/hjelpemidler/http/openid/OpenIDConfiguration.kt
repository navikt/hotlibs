package no.nav.hjelpemidler.http.openid

import kotlin.reflect.KProperty

internal object EnvironmentVariable {
    operator fun get(name: String): String? = System.getenv(name)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        checkNotNull(this[property.name]) {
            "Miljøvariabelen '${property.name}' mangler"
        }
}

object AzureADEnvironmentVariable {
    val AZURE_APP_CLIENT_ID by EnvironmentVariable
    val AZURE_APP_CLIENT_SECRET by EnvironmentVariable
    val AZURE_APP_JWKS by EnvironmentVariable
    val AZURE_APP_JWK by EnvironmentVariable
    val AZURE_APP_PRE_AUTHORIZED_APPS by EnvironmentVariable
    val AZURE_APP_TENANT_ID by EnvironmentVariable
    val AZURE_APP_WELL_KNOWN_URL by EnvironmentVariable
    val AZURE_OPENID_CONFIG_ISSUER by EnvironmentVariable
    val AZURE_OPENID_CONFIG_JWKS_URI by EnvironmentVariable
    val AZURE_OPENID_CONFIG_TOKEN_ENDPOINT by EnvironmentVariable
}

object TokenXEnvironmentVariable {
    val TOKEN_X_WELL_KNOWN_URL by EnvironmentVariable
    val TOKEN_X_CLIENT_ID by EnvironmentVariable
    val TOKEN_X_PRIVATE_JWK by EnvironmentVariable
    val TOKEN_X_ISSUER by EnvironmentVariable
    val TOKEN_X_JWKS_URI by EnvironmentVariable
    val TOKEN_X_TOKEN_ENDPOINT by EnvironmentVariable
}

interface OpenIDConfigurationProvider {
    val tokenEndpoint: String
    val clientId: String
    val clientSecret: String?
}

data class OpenIDConfiguration(
    override val tokenEndpoint: String,
    override val clientId: String,
    override val clientSecret: String?,
) : OpenIDConfigurationProvider

fun azureADEnvironmentConfiguration(): OpenIDConfigurationProvider = OpenIDConfiguration(
    tokenEndpoint = AzureADEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT,
    clientId = AzureADEnvironmentVariable.AZURE_APP_CLIENT_ID,
    clientSecret = AzureADEnvironmentVariable.AZURE_APP_CLIENT_SECRET,
)

fun tokenXEnvironmentConfiguration(): OpenIDConfigurationProvider = OpenIDConfiguration(
    tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT,
    clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID,
    clientSecret = null,
)
