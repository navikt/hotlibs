package no.nav.hjelpemidler.configuration

/**
 * @see <a href="https://docs.nais.io/auth/entra-id/reference/#runtime-variables-credentials">NAIS</a>
 */
object EntraIDEnvironmentVariable {
    @External
    val AZURE_APP_CLIENT_ID by EnvironmentVariable

    @External
    val AZURE_APP_CLIENT_SECRET by EnvironmentVariable

    @External
    val AZURE_APP_WELL_KNOWN_URL by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_ISSUER by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_JWKS_URI by EnvironmentVariable

    @External
    val AZURE_OPENID_CONFIG_TOKEN_ENDPOINT by EnvironmentVariable
}
