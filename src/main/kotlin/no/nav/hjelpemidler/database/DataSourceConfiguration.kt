package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import javax.sql.DataSource

private val log = LoggerFactory.getLogger(DataSourceConfiguration::class.java)

class DataSourceConfiguration internal constructor() {
    var hostname: String = "localhost"
    var port: Int = 5432
    var database: String? = null
    var username: String? = null
    var password: String? = null
    var envVarPrefix: String? = null

    private val hikariConfig = HikariConfig().also {
        it.connectionInitSql = "SET TIMEZONE TO 'UTC'"
    }

    fun hikari(block: HikariConfig.() -> Unit = {}) {
        hikariConfig.apply(block)
    }

    fun mappedPort(block: (Int) -> Int) {
        port = block(5432)
    }

    internal fun toDataSource(): DataSource {
        val cluster = System.getenv("NAIS_CLUSTER_NAME") ?: "local"
        when {
            envVarPrefix != null && cluster != "local" -> {
                log.info("Overskriver konfigurasjon med miljøvariabler, cluster: $cluster, envVarPrefix: $envVarPrefix")
                hostname = fromEnvVar("HOST")
                port = fromEnvVar("PORT").toInt()
                database = fromEnvVar("DATABASE")
                username = fromEnvVar("USERNAME")
                password = fromEnvVar("PASSWORD")
            }
        }
        log.info("Oppretter dataSource for hostname: $hostname, port: $port, database: $database")
        hikariConfig.dataSource = PGSimpleDataSource().also {
            it.serverNames = arrayOf(hostname)
            it.portNumbers = intArrayOf(port)
            it.databaseName = database
            it.user = username
            it.password = password
            it.reWriteBatchedInserts = true
        }
        return HikariDataSource(hikariConfig)
    }

    private fun fromEnvVar(envVarSuffix: String): String {
        val name = checkNotNull(envVarPrefix) {
            "Miljøvariabel-prefix (envVarPrefix i nais.yaml) er ikke satt"
        } + "_" + envVarSuffix
        return checkNotNull(System.getenv(name)) {
            "Miljøvariabelen '$name' mangler"
        }
    }
}
