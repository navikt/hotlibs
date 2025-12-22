package no.nav.hjelpemidler.database.jdbc

import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.PageRequest
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.UpdateResult
import no.nav.hjelpemidler.database.list
import no.nav.hjelpemidler.database.pgJsonbOf
import no.nav.hjelpemidler.database.single
import no.nav.hjelpemidler.database.sql.Sql
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestId
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.test.toTestEntity
import no.nav.hjelpemidler.database.toQueryParameters
import no.nav.hjelpemidler.database.transaction
import no.nav.hjelpemidler.serialization.jackson.valueToTree
import java.time.Instant
import java.time.ZonedDateTime
import kotlin.test.Test

class SessionJdbcOperationsTest {
    @Test
    fun `Henter ett innslag`() = runTest {
        val nyEntity = TestEntity()
        val id = transaction(testDataSource) { it.lagre(nyEntity) }
        val lagretEntity = transaction(testDataSource) { it.hent(id) }
        lagretEntity shouldBe nyEntity.copy(id = id)
    }

    @Test
    fun `Henter flere innslag`() = runTest {
        val ids = lagreEntities(10)
        val result = transaction(testDataSource) {
            it.list<Long>(
                sql = "SELECT id FROM test WHERE id = ANY (:ids)",
                queryParameters = mapOf("ids" to ids.toTypedArray()),
            )
        }

        result shouldBe ids
    }

    @Test
    fun `Henter side`() = runTest {
        val ids = lagreEntities(20)
        val result = transaction(testDataSource) { tx ->
            tx.page(
                sql = "SELECT $columns, COUNT(1) OVER() AS total_elements FROM test WHERE id = ANY (:ids) ORDER BY id",
                queryParameters = mapOf("ids" to ids.toTypedArray()),
                pageRequest = PageRequest(1, 5),
                mapper = Row::toTestEntity,
            )
        }

        result shouldHaveSize 5
        result.totalElements shouldBe ids.size
        result.map { it.id.value } shouldBe ids.subList(0, 5)
    }

    @Test
    fun `Henter JSON`() = runTest {
        val nyEntity = TestEntity(data = null)
        val id = transaction(testDataSource) { it.lagre(nyEntity) }
        val result = transaction(testDataSource) { it.hent(id) }

        result.data shouldBe nyEntity.data
    }

    @Test
    fun `Oppdaterer innslag`() = runTest {
        val id = lagre()
        val result = transaction(testDataSource) {
            it.update(
                sql = "UPDATE test SET integer = :integer WHERE id = :id",
                queryParameters = mapOf("integer" to 50, "id" to id),
            )
        }

        result shouldBe UpdateResult(1)
    }

    @Test
    fun `Sletter innslag`() = runTest {
        val id = lagre()
        val result = transaction(testDataSource) {
            it.update(
                sql = "DELETE FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }

        result shouldBe UpdateResult(1)
    }

    @Test
    fun `Setter inn flere innslag`() = runTest {
        val items = listOf(
            TestEntity(string = "x1", integer = 1, enum = TestEnum.A, data = valueToTree(mapOf("key" to "t1"))),
            TestEntity(string = "x2", integer = 2, enum = TestEnum.B, data = valueToTree(mapOf("key" to "t2"))),
            TestEntity(string = "x3", integer = 3, enum = TestEnum.C, data = valueToTree(mapOf("key" to "t3"))),
        )

        val rows1 = transaction(testDataSource) { tx ->
            tx.batch(insertSql, items) {
                it.toQueryParameters() + mapOf(
                    "data" to pgJsonbOf(it.data),
                ) + it.navn.toQueryParameters()
            }
        }

        rows1 shouldHaveSize 3

        val rows2 = transaction(testDataSource) { tx ->
            tx.list(
                "SELECT $columns FROM test WHERE string LIKE 'x%'",
                mapper = Row::toTestEntity
            )
        }

        rows2 shouldHaveSize 3
    }

    @Test
    fun `Skal sette inn og hente null som null`() = runTest {
        val id = transaction(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data) VALUES (:data) RETURNING id""",
                queryParameters = mapOf("data" to pgJsonbOf(null)),
            )
        }
        val result = transaction(testDataSource) { it.hent(id) }

        result.data.shouldBeNull()
    }

    @Test
    fun `Skal sette inn og hente MissingNode som null`() = runTest {
        val id = transaction(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data) VALUES (:data) RETURNING id""",
                queryParameters = mapOf("data" to pgJsonbOf(MissingNode.getInstance())),
            )
        }
        val result = transaction(testDataSource) { it.hent(id) }

        result.data.shouldBeNull()
    }

    @Test
    fun `Skal sette inn og hente NullNode som null`() = runTest {
        val id = transaction(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data) VALUES (:data) RETURNING id""",
                queryParameters = mapOf("data" to pgJsonbOf(NullNode.getInstance())),
            )
        }
        val result = transaction(testDataSource) { it.hent(id) }

        result.data.shouldBeNull()
    }

    @Test
    fun `Skal hente som JsonNode`() = runTest {
        val id = lagre()
        val result = transaction(dataSource = testDataSource) {
            it.single(
                sql = "SELECT $columns FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
                mapper = Row::asTree,
            )
        }

        result["id"]?.longValue() shouldBe id.value
    }

    @Test
    fun `Skal hente som TestId`() = runTest {
        val id = lagre()
        val result = transaction(dataSource = testDataSource) {
            it.single<TestId>(
                sql = "SELECT id FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }

        result.shouldBeInstanceOf<TestId>()
    }

    @Test
    fun `Skal hente som TestEnum`() = runTest {
        val id = lagre()
        val result = transaction(dataSource = testDataSource) {
            it.single<TestEnum>(
                sql = "SELECT enum FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }

        result shouldBe TestEnum.A
    }

    @Test
    fun `Skal hente som TestEntity`() = runTest {
        val nyEntity = TestEntity(
            data = valueToTree(mapOf("k1" to 1, "k2" to "2")),
        )
        val id = transaction(testDataSource) { it.lagre(nyEntity) }
        val lagretEntity = transaction(testDataSource) {
            it.single<TestEntity>(
                sql = "SELECT $columns FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }
        lagretEntity shouldBe nyEntity.copy(id = id)
    }

    @Test
    fun `Skal hente personnavn som JsonNode`() = runTest {
        val id = lagre()
        val result = transaction(testDataSource) {
            it.single("SELECT (navn).* FROM test WHERE id = :id", id.toQueryParameters("id")) { row ->
                row.asTree()
            }
        }
        result.shouldBeInstanceOf<ObjectNode>()
    }

    @Test
    fun `Skal hente timestamp som Instant`() = runTest {
        val id = lagre()
        val result = hent<Instant>(id, "timestamp_with_timezone")
        result.shouldBeInstanceOf<Instant>()
    }

    @Test
    fun `Skal hente timestamp som ZonedDateTime`() = runTest {
        val id = lagre()
        val result = hent<ZonedDateTime>(id, "timestamp_with_timezone")
        result.shouldBeInstanceOf<ZonedDateTime>()
    }

    private suspend fun lagre(): TestId = transaction(testDataSource) { it.lagre() }

    private suspend inline fun <reified T : Any> hent(id: TestId, vararg columns: String): T =
        transaction(testDataSource) {
            it.single<T>(
                sql = """
                    SELECT ${columns.joinToString(", ")}
                    FROM test
                    WHERE id = :id
                """.trimIndent(),
                queryParameters = id.toQueryParameters("id"),
            )
        }

    private suspend fun lagreEntities(antall: Int): List<Long> = transaction(testDataSource) { tx ->
        tx.batchAndReturnGeneratedKeys(
            sql = "INSERT INTO test DEFAULT VALUES RETURNING id",
            queryParameters = (1..antall).map { emptyMap() },
        )
    }
}

private const val columns =
    "id, boolean, enum, integer, long, string, uuid, date, time, time_with_timezone, timestamp, timestamp_with_timezone, fnr, aktor_id, (navn).*, strings, integers, data"

private val insertSql = Sql(
    """
        INSERT INTO test (boolean, enum, integer, long, string, uuid, date, time, time_with_timezone, timestamp,
                          timestamp_with_timezone, fnr, aktor_id, navn, strings, integers, data)
        VALUES (:boolean, :enum, :integer, :long, :string, :uuid, :date, :time, :time_with_timezone, :timestamp,
                :timestamp_with_timezone, :fnr, :aktor_id, (:fornavn,:mellomnavn,:etternavn), :strings, :integers, :data)
        RETURNING id
    """.trimIndent(),
)

private fun JdbcOperations.lagre(): TestId = single<TestId>("INSERT INTO test DEFAULT VALUES RETURNING id")
private fun JdbcOperations.lagre(entity: TestEntity): TestId {
    val navn = entity.navn
    return single<TestId>(
        sql = insertSql,
        queryParameters = entity.toQueryParameters() + mapOf(
            "data" to pgJsonbOf(entity.data),
        ) + navn.toQueryParameters(),
    )
}

private fun JdbcOperations.hent(id: TestId) = single(
    sql = "SELECT $columns FROM test WHERE id = :id",
    queryParameters = id.toQueryParameters("id"),
    mapper = Row::toTestEntity,
)
