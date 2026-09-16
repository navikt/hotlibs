package no.nav.hjelpemidler.configuration

/**
 * @see <a href="https://docs.nais.io/services/feature-toggling/">Feature Toggling - Nais</a>
 */
object UnleashEnvironmentVariable {
    @External
    val UNLEASH_SERVER_API_ENV by EnvironmentVariable

    @External
    val UNLEASH_SERVER_API_PROJECTS by EnvironmentVariable

    @External
    val UNLEASH_SERVER_API_TOKEN by EnvironmentVariable

    @External
    val UNLEASH_SERVER_API_TYPE by EnvironmentVariable

    @External
    val UNLEASH_SERVER_API_URL by EnvironmentVariable
}
