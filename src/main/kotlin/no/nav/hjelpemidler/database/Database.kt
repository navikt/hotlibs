package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

interface Database : DataSource {
    fun clean(): Database
    fun migrate(): Database
}

fun createDatabase(
    jdbcUrl: String,
    username: String,
    password: String,
    driverClassName: String = "org.postgresql.Driver",
    cleanDisabled: Boolean = true,
): Database {
    val dataSource = HikariDataSource(
        HikariConfig()
            .apply {
                this.jdbcUrl = jdbcUrl
                this.username = username
                this.password = password
                this.driverClassName = driverClassName
            }
    )

    val flyway = Flyway
        .configure()
        .dataSource(dataSource)
        .cleanDisabled(cleanDisabled)
        .load()

    return object : Database, DataSource by dataSource {
        override fun clean(): Database {
            flyway.clean()
            return this
        }

        override fun migrate(): Database {
            flyway.migrate()
            return this
        }
    }
}
