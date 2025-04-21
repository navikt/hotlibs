package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.configuration.Configuration

class PostgreSQLDataSourceConfiguration internal constructor() : DataSourceConfiguration() {
    var envVarPrefix: String? = null

    var hostname: String = "localhost"
    var port: Int = 5432

    // SSL
    var sslMode: String? = null
    var sslCert: String? = null
    var sslKey: String? = null
    var sslRootCert: String? = null

    init {
        connectionInitSql = "SET TIMEZONE TO 'UTC'"
    }

    internal fun fromEnvVar(envVarSuffix: String, optional: Boolean = false): String? {
        val key = checkNotNull(envVarPrefix) {
            "Miljøvariabel-prefix (envVarPrefix i nais.yaml) er ikke satt"
        } + "_" + envVarSuffix
        val value = Configuration[key]
        if (optional) return value
        return checkNotNull(value) {
            "Miljøvariabelen '$key' mangler"
        }
    }
}
