package no.nav.hjelpemidler.database

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import javax.sql.DataSource

fun createDataSource(block: DataSourceConfiguration.() -> Unit = {}): DataSource {
    val configuration = DataSourceConfiguration().apply(block)
    return configuration.toDataSource()
}

fun createFlyway(dataSource: DataSource, block: ClassicConfiguration.() -> Unit = {}): Flyway =
    Flyway.configure()
        .configuration(ClassicConfiguration().apply(block))
        .dataSource(dataSource)
        .load()
