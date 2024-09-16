package no.nav.hjelpemidler.configuration

/**
 * @see <a href="https://docs.nais.io/auth/tokenx/reference/#runtime-variables-credentials">NAIS</a>
 */
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
