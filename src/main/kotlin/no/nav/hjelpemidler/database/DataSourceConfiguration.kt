package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import org.postgresql.jdbcurlresolver.PgPassParser

class DataSourceConfiguration internal constructor() : HikariConfig() {
    var hostname: String? = null
    var port: Int? = null
    var database: String? = null

    fun postgreSQL(usePasswordFile: Boolean = false) {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://$hostname:$port/$database?reWriteBatchedInserts=true"
        connectionInitSql = "SET TIMEZONE TO 'UTC'"

        if (usePasswordFile) {
            password = PgPassParser.getPassword(
                hostname,
                port.toString(),
                database,
                username,
            )
        }
    }
}
