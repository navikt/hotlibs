package no.nav.hjelpemidler.database.test

import kotliquery.Session
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.flyway
import no.nav.hjelpemidler.database.transaction
import javax.sql.DataSource

fun testDataSource(): DataSource = createDataSource(
    jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
    username = "",
    password = "",
    driverClassName = "org.h2.Driver"
).flyway {
    migrate()
}

fun <T> testTransaction(block: (Session) -> T): T =
    transaction(dataSource = testDataSource(), block = block)
