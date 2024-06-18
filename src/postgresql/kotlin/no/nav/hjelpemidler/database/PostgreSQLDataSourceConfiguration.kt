package no.nav.hjelpemidler.database

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

    // fixme -> lag en løsning i hm-core for dette
    internal fun fromEnvVar(envVarSuffix: String, optional: Boolean = false): String? {
        val name = checkNotNull(envVarPrefix) {
            "Miljøvariabel-prefix (envVarPrefix i nais.yaml) er ikke satt"
        } + "_" + envVarSuffix
        val value = System.getenv(name)
        if (optional) return value
        return checkNotNull(value) {
            "Miljøvariabelen '$name' mangler"
        }
    }
}
