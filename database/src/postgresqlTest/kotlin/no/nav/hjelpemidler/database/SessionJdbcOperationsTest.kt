package no.nav.hjelpemidler.database

import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.sql.Sql
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestId
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.test.toTestEntity
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import kotlin.test.Test

class SessionJdbcOperationsTest {
    @Test
    fun `Henter ett innslag`() = runTest {
        val nyEntity = TestEntity()
        val id = transactionAsync(testDataSource) { it.lagre(nyEntity) }
        val lagretEntity = transactionAsync(testDataSource) { it.hent(id) }
        lagretEntity shouldBe nyEntity.copy(id = id)
    }

    @Test
    fun `Henter flere innslag`() = runTest {
        val ids = lagreEntities(10)
        val result = transactionAsync(testDataSource) {
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
        val result = transactionAsync(testDataSource) { tx ->
            tx.page(
                sql = "SELECT *, COUNT(1) OVER() AS total_elements FROM test WHERE id = ANY (:ids) ORDER BY id",
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
        val nyEntity = TestEntity(dataRequired = mapOf("k1" to "v1"))
        val id = transactionAsync(testDataSource) { it.lagre(nyEntity) }
        val result = transactionAsync(testDataSource) { it.hent(id) }

        result.dataRequired shouldBe nyEntity.dataRequired
    }

    @Test
    fun `Oppdaterer innslag`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val result = transactionAsync(testDataSource) {
            it.update(
                sql = "UPDATE test SET integer = :integer WHERE id = :id",
                queryParameters = mapOf("integer" to 50, "id" to id),
            )
        }

        result shouldBe UpdateResult(1)
    }

    @Test
    fun `Sletter innslag`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val result = transactionAsync(testDataSource) {
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
            TestEntity(string = "x1", integer = 1, enum = TestEnum.A, dataRequired = mapOf("key" to "t1")),
            TestEntity(string = "x2", integer = 2, enum = TestEnum.B, dataRequired = mapOf("key" to "t2")),
            TestEntity(string = "x3", integer = 3, enum = TestEnum.C, dataRequired = mapOf("key" to "t3")),
        )

        val rows1 = transactionAsync(testDataSource) { tx ->
            tx.batch(insertSql, items) {
                it.toQueryParameters() + mapOf(
                    "data_required" to pgJsonbOf(it.dataRequired),
                    "data_optional" to pgJsonbOf(it.dataOptional),
                )
            }
        }

        rows1 shouldHaveSize 3

        val rows2 = transactionAsync(testDataSource) { tx ->
            tx.list("SELECT * FROM test WHERE string LIKE 'x%'", mapper = Row::toTestEntity)
        }

        rows2 shouldHaveSize 3
    }

    @Test
    fun `Skal sette inn og hente null som null`() = runTest {
        val id = transactionAsync(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data_optional) VALUES (:dataOptional) RETURNING id""",
                queryParameters = mapOf("dataOptional" to pgJsonbOf(null)),
            )
        }
        val result = transactionAsync(testDataSource) { it.hent(id) }

        result.dataOptional.shouldBeNull()
    }

    @Test
    fun `Skal sette inn og hente MissingNode som null`() = runTest {
        val id = transactionAsync(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data_optional) VALUES (:dataOptional) RETURNING id""",
                queryParameters = mapOf("dataOptional" to pgJsonbOf(MissingNode.getInstance())),
            )
        }
        val result = transactionAsync(testDataSource) { it.hent(id) }

        result.dataOptional.shouldBeNull()
    }

    @Test
    fun `Skal sette inn og hente NullNode som null`() = runTest {
        val id = transactionAsync(testDataSource) {
            it.single<TestId>(
                sql = """INSERT INTO test (data_optional) VALUES (:dataOptional) RETURNING id""",
                queryParameters = mapOf("dataOptional" to pgJsonbOf(NullNode.getInstance())),
            )
        }
        val result = transactionAsync(testDataSource) { it.hent(id) }

        result.dataOptional.shouldBeNull()
    }

    @Test
    fun `Skal hente som JsonNode`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val result = transactionAsync(dataSource = testDataSource) {
            it.single(
                sql = "SELECT * FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
                mapper = Row::toTree,
            )
        }

        result["id"]?.longValue() shouldBe id.value
    }

    @Test
    fun `Skal hente som TestId`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val result = transactionAsync(dataSource = testDataSource) {
            it.single<TestId>(
                sql = "SELECT id FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }

        result.shouldBeInstanceOf<TestId>()
    }

    @Test
    fun `Skal hente som TestEnum`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val result = transactionAsync(dataSource = testDataSource) {
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
            dataRequired = mapOf("k1" to 1, "k2" to "2"),
            dataOptional = jsonMapper.createObjectNode(),
            navn = null,
        )
        val id = transactionAsync(testDataSource) { it.lagre(nyEntity) }
        val lagretEntity = transactionAsync(testDataSource) {
            it.single<TestEntity>(
                sql = "SELECT * FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters("id"),
            )
        }
        lagretEntity shouldBe nyEntity.copy(id = id)
    }

    private suspend fun lagreEntities(antall: Int): List<Long> = transactionAsync(testDataSource) { tx ->
        tx.batchAndReturnGeneratedKeys(
            sql = "INSERT INTO test DEFAULT VALUES RETURNING id",
            queryParameters = (1..antall).map { emptyMap() },
        )
    }
}

private val insertSql = Sql(
    """
        INSERT INTO test (integer, long, string, enum, data_required, data_optional, fnr, aktor_id, navn, boolean_required,
                          boolean_optional, date, time, time_with_timezone, timestamp, timestamp_with_timezone, array_string,
                          array_integer, uuid)
        VALUES (:integer, :long, :string, :enum, :data_required, :data_optional, :fnr, :aktor_id,
                (:fornavn, :mellomnavn, :etternavn)::gyldig_personnavn,
                :boolean_required, :boolean_optional, :date, :time, :time_with_timezone, :timestamp,
                :timestamp_with_timezone, :array_string, :array_integer, :uuid)
        RETURNING id
    """.trimIndent(),
)

private fun JdbcOperations.lagre(): TestId = single<TestId>("INSERT INTO test DEFAULT VALUES RETURNING id")
private fun JdbcOperations.lagre(entity: TestEntity): TestId = single<TestId>(
    sql = insertSql,
    queryParameters = entity.toQueryParameters() + mapOf(
        "data_required" to pgJsonbOf(entity.dataRequired),
        "data_optional" to pgJsonbOf(entity.dataOptional),
    ),
)

private fun JdbcOperations.hent(id: TestId) = single(
    sql = "SELECT * FROM test WHERE id = :id",
    queryParameters = id.toQueryParameters("id"),
    mapper = Row::toTestEntity,
)
