package no.nav.hjelpemidler.configuration

/**
 * @see <a href="https://docs.nais.io/auth/maskinporten/reference/#runtime-variables-credentials">NAIS</a>
 */
object MaskinportenEnvironmentVariable {
    @External
    val MASKINPORTEN_CLIENT_ID by EnvironmentVariable

    @External
    val MASKINPORTEN_CLIENT_JWK by EnvironmentVariable

    @External
    val MASKINPORTEN_ISSUER by EnvironmentVariable

    @External
    val MASKINPORTEN_JWKS_URI by EnvironmentVariable

    @External
    val MASKINPORTEN_SCOPES by EnvironmentVariable

    @External
    val MASKINPORTEN_TOKEN_ENDPOINT by EnvironmentVariable

    @External
    val MASKINPORTEN_WELL_KNOWN_URL by EnvironmentVariable
}
