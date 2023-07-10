package no.nav.hjelpemidler.database

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.CleanResult
import org.flywaydb.core.api.output.MigrateResult
import javax.sql.DataSource

fun DataSource.flyway(block: FluentConfiguration.() -> Unit = {}): Flyway =
    Flyway.configure()
        .dataSource(this)
        .loggers("slf4j")
        .apply(block)
        .load()

fun DataSource.clean(block: FluentConfiguration.() -> Unit = {}): CleanResult? =
    flyway(block).clean()

fun DataSource.migrate(block: FluentConfiguration.() -> Unit = {}): MigrateResult =
    flyway(block).migrate()
