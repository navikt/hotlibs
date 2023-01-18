package no.nav.hjelpemidler.database.test

import kotliquery.Session
import no.nav.hjelpemidler.database.Database
import no.nav.hjelpemidler.database.createDatabase
import no.nav.hjelpemidler.database.transaction
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import javax.sql.DataSource
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class AbstractDatabaseTest {
    private companion object {
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:15-alpine")
                .waitingFor(Wait.forListeningPort())
                .also {
                    it.start()
                }

        val database: Database =
            createDatabase(
                jdbcUrl = container.jdbcUrl,
                username = container.username,
                password = container.password,
                cleanDisabled = false,
            )
    }

    @BeforeTest
    fun beforeTest() {
        database.migrate()
    }

    @AfterTest
    fun afterTest() {
        database.clean()
    }

    fun <T> testTransaction(dataSource: DataSource = database, block: (Session) -> T): T =
        transaction(dataSource = dataSource, block = block)
}
