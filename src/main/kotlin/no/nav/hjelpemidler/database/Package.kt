package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import javax.sql.DataSource

fun createDataSource(block: DataSourceConfiguration.() -> Unit = {}): DataSource =
    HikariDataSource(DataSourceConfiguration().apply(block))

fun createMigrator(dataSource: DataSource, block: ClassicConfiguration.() -> Unit = {}): Migrator =
    FlywayMigrator(
        Flyway.configure()
            .configuration(ClassicConfiguration().apply(block))
            .dataSource(dataSource)
            .load()
    )
