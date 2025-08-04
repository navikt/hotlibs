package no.nav.hjelpemidler.configuration

object TexasEnvironmentVariable {
    val TEXAS_ENABLED by EnvironmentVariable(defaultValue = false)
    val NAIS_TOKEN_ENDPOINT by EnvironmentVariable
    val NAIS_TOKEN_EXCHANGE_ENDPOINT by EnvironmentVariable
    val NAIS_TOKEN_INTROSPECTION_ENDPOINT by EnvironmentVariable
}
