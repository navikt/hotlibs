package no.nav.hjelpemidler.database

import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(PostgreSQL::class.java)

object PostgreSQL : DataSourceConfigurationFactory<PostgreSQLDataSourceConfiguration> {
    override fun invoke(block: PostgreSQLDataSourceConfiguration.() -> Unit): PostgreSQLDataSourceConfiguration =
        PostgreSQLDataSourceConfiguration()
            .apply(block)
            .apply {
                if (envVarPrefix != null && cluster !in setOf("local", "test")) {
                    log.info("Overskriver konfigurasjon med miljøvariabler, cluster: $cluster, envVarPrefix: $envVarPrefix")
                    hostname = fromEnvVar("HOST") ?: hostname
                    port = fromEnvVar("PORT")?.toInt() ?: port
                    databaseName = fromEnvVar("DATABASE")
                    username = fromEnvVar("USERNAME")
                    password = fromEnvVar("PASSWORD")

                    sslMode = fromEnvVar("SSLMODE", true)
                    sslCert = fromEnvVar("SSLCERT", true)
                    sslKey = fromEnvVar("SSLKEY_PK8", true)
                    sslRootCert = fromEnvVar("SSLROOTCERT", true)
                }

                log.info("Oppretter dataSource for hostname: $hostname, port: $port, database: $databaseName")
                dataSource = PGSimpleDataSource().also {
                    it.serverNames = arrayOf(hostname)
                    it.portNumbers = intArrayOf(port)
                    it.databaseName = databaseName
                    it.user = username
                    it.password = password
                    it.reWriteBatchedInserts = true

                    // Setup SSL if available
                    sslMode?.let { sslMode -> it.sslMode = sslMode }
                    sslCert?.let { sslCert -> it.sslCert = sslCert }
                    sslKey?.let { sslKey -> it.sslKey = sslKey }
                    sslRootCert?.let { sslRootCert -> it.sslRootCert = sslRootCert }
                }
            }
}
