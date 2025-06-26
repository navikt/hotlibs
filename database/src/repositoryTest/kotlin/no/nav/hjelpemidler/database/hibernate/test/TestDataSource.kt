package no.nav.hjelpemidler.database.hibernate.test

import no.nav.hjelpemidler.database.Testcontainers
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.migrate
import javax.sql.DataSource

val testDataSource: DataSource by lazy {
    createDataSource(Testcontainers) {
        tag = "15-alpine"
    }.apply(DataSource::migrate)
}
