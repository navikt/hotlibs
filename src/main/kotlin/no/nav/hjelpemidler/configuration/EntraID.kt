package no.nav.hjelpemidler.configuration

object EntraID {
    @External
    val AZURE_APP_CLIENT_ID by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_ISSUER by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_JWKS_URI by EnvironmentVariable
}
