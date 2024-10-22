package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.Testcontainers
import no.nav.hjelpemidler.database.createDataSource
import javax.sql.DataSource

val testDataSource: DataSource by lazy {
    createDataSource(Testcontainers) {
        tag = "15-alpine"
    }
}
