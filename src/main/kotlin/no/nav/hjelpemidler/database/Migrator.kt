package no.nav.hjelpemidler.database

import org.flywaydb.core.Flyway

interface Migrator {
    fun clean()
    fun migrate()
}

internal class FlywayMigrator(private val flyway: Flyway) : Migrator {
    override fun clean() {
        flyway.clean()
    }

    override fun migrate() {
        flyway.migrate()
    }
}
