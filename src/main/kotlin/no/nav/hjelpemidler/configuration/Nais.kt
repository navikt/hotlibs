package no.nav.hjelpemidler.configuration

object NaisEnvironmentVariable {
    @External
    val NAIS_APP_IMAGE by EnvironmentVariable

    @External
    val NAIS_APP_NAME by EnvironmentVariable

    @External
    val NAIS_CLIENT_ID by EnvironmentVariable

    @External
    val NAIS_CLUSTER_NAME by EnvironmentVariable

    @External
    val NAIS_NAMESPACE by EnvironmentVariable

    @External
    val NAV_TRUSTSTORE_PASSWORD by EnvironmentVariable

    @External
    val NAV_TRUSTSTORE_PATH by EnvironmentVariable
}
