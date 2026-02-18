package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.configuration.Environment
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.CleanResult
import org.flywaydb.core.api.output.MigrateResult
import org.flywaydb.core.api.output.RepairResult
import org.flywaydb.core.api.output.ValidateResult
import javax.sql.DataSource

fun DataSource.flyway(block: FluentConfiguration.() -> Unit = {}): Flyway =
    Flyway.configure()
        .dataSource(this)
        .loggers("slf4j")
        .apply(block)
        .load()

fun DataSource.clean(block: FluentConfiguration.() -> Unit = {}): CleanResult {
    check(!Environment.current.isProd) { "Ikke kjør Flyway clean i produksjon!" }
    return flyway(block).clean()
}

fun DataSource.migrate(block: FluentConfiguration.() -> Unit = {}): MigrateResult =
    flyway(block).migrate()

fun DataSource.repair(block: FluentConfiguration.() -> Unit = {}): RepairResult =
    flyway(block).repair()

fun DataSource.validate(block: FluentConfiguration.() -> Unit = {}): Unit =
    flyway(block).validate()

fun DataSource.validateWithResult(block: FluentConfiguration.() -> Unit = {}): ValidateResult =
    flyway(block).validateWithResult()
