package no.nav.hjelpemidler.database.test

import kotliquery.Session
import no.nav.hjelpemidler.database.createDatabase
import no.nav.hjelpemidler.database.transaction
import javax.sql.DataSource

fun testDataSource(): DataSource = createDatabase(
    jdbcUrl = "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
    username = "",
    password = "",
    driverClassName = "org.h2.Driver",
    cleanDisabled = false,
).clean().migrate()

fun <T> testTransaction(dataSource: DataSource = testDataSource(), block: (Session) -> T): T =
    transaction(dataSource = dataSource, block = block)
