package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.ds.common.BaseDataSource
import org.slf4j.LoggerFactory
import javax.sql.DataSource

private val log = LoggerFactory.getLogger(DataSourceConfiguration::class.java)

class DataSourceConfiguration internal constructor() {
    private val dataSource = PGSimpleDataSource().also {
        it.reWriteBatchedInserts = true
    }
    private val hikariConfig = HikariConfig().also {
        it.dataSource = dataSource
        it.connectionInitSql = "SET TIMEZONE TO 'UTC'"
    }

    var envVarPrefix: String? = null

    var hostname: String
        get() = dataSource.serverNames.single()
        set(value) {
            dataSource.serverNames = arrayOf(value)
        }

    var port: Int
        get() = dataSource.portNumbers.single()
        set(value) {
            dataSource.portNumbers = intArrayOf(value)
        }

    var database: String?
        get() = dataSource.databaseName
        set(value) {
            dataSource.databaseName = value
        }

    var username: String?
        get() = dataSource.user
        set(value) {
            dataSource.user = value
        }

    var password: String?
        get() = dataSource.password
        set(value) {
            dataSource.password = value
        }

    internal fun postgres(block: BaseDataSource.() -> Unit = {}) {
        dataSource.apply(block)
    }

    fun hikari(block: HikariConfig.() -> Unit = {}) {
        hikariConfig.apply(block)
    }

    fun toDataSource(): DataSource {
        val cluster = System.getenv("NAIS_CLUSTER_NAME") ?: "local"
        when {
            envVarPrefix != null && cluster != "local" -> {
                log.info("Overskriver konfigurasjon med miljøvariabler, cluster: $cluster")
                hostname = fromEnvVar("HOST")
                port = fromEnvVar("PORT").toInt()
                database = fromEnvVar("DATABASE")
                username = fromEnvVar("USERNAME")
                password = fromEnvVar("PASSWORD")
            }
        }
        log.info("Oppretter dataSource for hostname: $hostname, port: $port, database: $database")
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
