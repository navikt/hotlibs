package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.migrate
import javax.sql.DataSource

val testDataSource: DataSource by lazy {
    createDataSource {
        testcontainers("15-alpine")
    }.also {
        it.migrate()
    }
}
