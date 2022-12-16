package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun createDataSource(
    jdbcUrl: String,
    username: String,
    password: String,
    driverClassName: String = "org.postgresql.Driver",
    configure: (HikariConfig).() -> Unit = {},
): DataSource = HikariDataSource(
    HikariConfig()
        .apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            this.driverClassName = driverClassName
        }
        .apply(configure)
)

fun DataSource.flyway(block: Flyway.() -> Unit): DataSource = this.apply {
    Flyway
        .configure()
        .dataSource(this)
        .load()
        .apply(block)
}
