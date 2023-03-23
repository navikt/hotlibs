package no.nav.hjelpemidler.database.test

import kotliquery.TransactionalSession
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.createFlyway
import no.nav.hjelpemidler.database.transaction
import org.flywaydb.core.Flyway
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import javax.sql.DataSource
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.time.Duration.Companion.minutes

abstract class AbstractDatabaseTest {
    private companion object {
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:15-alpine")
                .waitingFor(Wait.forListeningPort())
                .also {
                    it.start()
                }

        val dataSource: DataSource =
            createDataSource {
                hostname = container.host
                mappedPort(container::getMappedPort)
                database = container.databaseName
                username = container.username
                password = container.password

                hikari {
                    connectionTimeout = 1.minutes.inWholeMilliseconds
                }
            }

        val flyway: Flyway =
            createFlyway(dataSource) {
                isCleanDisabled = false
            }
    }

    val storeContext = TestStoreContext(dataSource)

    @BeforeTest
    fun beforeTest() {
        flyway.migrate()
    }

    @AfterTest
    fun afterTest() {
        flyway.clean()
    }

    suspend fun <T> testTransaction(
        dataSource: DataSource = AbstractDatabaseTest.dataSource,
        returnGeneratedKey: Boolean = false,
        block: suspend (TransactionalSession) -> T,
    ): T =
        transaction(
            dataSource = dataSource,
            returnGeneratedKey = returnGeneratedKey,
            block = block,
        )
}
