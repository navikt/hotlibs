package no.nav.hjelpemidler.configuration

/**
 * @see <a href="https://docs.nais.io/auth/idporten/reference/#runtime-variables-credentials">NAIS</a>
 */
object IDPortenEnvironmentVariable {
    @External
    val IDPORTEN_AUDIENCE by EnvironmentVariable

    @External
    val IDPORTEN_ISSUER by EnvironmentVariable

    @External
    val IDPORTEN_JWKS_URI by EnvironmentVariable

    @External
    val IDPORTEN_WELL_KNOWN_URL by EnvironmentVariable
}
