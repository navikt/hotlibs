package no.nav.hjelpemidler.database.test

import kotliquery.TransactionalSession
import no.nav.hjelpemidler.database.Migrator
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.createMigrator
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

        val dataSource: DataSource =
            createDataSource {
                driverClassName = container.driverClassName
                jdbcUrl = container.jdbcUrl
                username = container.username
                password = container.password
                connectionInitSql = "SET TIMEZONE TO 'UTC'"
            }

        val migrator: Migrator =
            createMigrator(dataSource) {
                isCleanDisabled = false
            }
    }

    val storeContext = TestStoreContext(dataSource)

    @BeforeTest
    fun beforeTest() {
        migrator.migrate()
    }

    @AfterTest
    fun afterTest() {
        migrator.clean()
    }

    fun <T> testTransaction(
        dataSource: DataSource = AbstractDatabaseTest.dataSource,
        returnGeneratedKey: Boolean = false,
        block: (TransactionalSession) -> T,
    ): T =
        transaction(
            dataSource = dataSource,
            returnGeneratedKey = returnGeneratedKey,
            block = block,
        )
}
