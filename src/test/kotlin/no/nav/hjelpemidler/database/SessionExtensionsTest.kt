package no.nav.hjelpemidler.database

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestStore
import no.nav.hjelpemidler.database.test.testDataSource
import kotlin.test.Test

class SessionExtensionsTest {
    @Test
    fun `henter ett innslag`() = runTest {
        val id = lagreEntity()
        val sql = "SELECT id FROM test WHERE id = :id"
        val queryParameters = id.toQueryParameters()

        transaction(testDataSource) { tx ->
            tx.query(sql = sql, queryParameters = queryParameters) { row ->
                row.long("id")
            }
        } shouldBe id

        transaction(testDataSource) { tx ->
            tx.query(sql = sql, queryParameters = 0.toQueryParameters()) { row ->
                row.long("id")
            }
        } shouldBe null

        shouldNotThrow<NoSuchElementException> {
            transaction(testDataSource) { tx ->
                tx.single(sql = sql, queryParameters = queryParameters) { row ->
                    row.long("id")
                }
            }
        }
    }

    @Test
    fun `henter flere innslag`() = runTest {
        val ids = lagreEntities(10)
        val result = transaction(testDataSource) { tx ->
            tx.queryList(
                sql = "SELECT * FROM test WHERE id = ANY(:ids)",
                queryParameters = mapOf(
                    "ids" to ids.toTypedArray()
                ),
            ) {
                it.toMap()
            }
        }

        result shouldBeSameSizeAs ids
    }

    @Test
    fun `henter side`() = runTest {
        val ids = lagreEntities(20)
        val result = transaction(testDataSource) { tx ->
            tx.queryPage(
                sql = """
                    SELECT *, COUNT(1) OVER() AS total
                    FROM test
                    WHERE id = ANY(:ids)
                """.trimIndent(),
                queryParameters = mapOf(
                    "ids" to ids.toTypedArray()
                ),
                limit = 5,
                offset = 0,
            ) {
                it.toMap()
            }
        }

        result shouldHaveSize 5
        result.total shouldBe ids.size
    }

    @Test
    fun `henter json`() = runTest {
        val id = lagreEntity()
        val result = transaction(testDataSource) { tx ->
            tx.single(
                sql = "SELECT data_1 FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters(),
            ) {
                it.json<Map<String, Any?>>("data_1")
            }
        }

        result.shouldContain("key", "value")
    }

    @Test
    fun `oppdaterer innslag`() = runTest {
        val id = lagreEntity()
        val result = transaction(testDataSource) { tx ->
            tx.update(
                sql = "UPDATE test SET integer = 50 WHERE id = :id",
                queryParameters = id.toQueryParameters()
            )
        }

        shouldNotThrow<IllegalStateException> {
            result.expect(1)
        }
    }

    @Test
    fun `sletter innslag`() = runTest {
        val id = lagreEntity()
        val result = transaction(testDataSource) { tx ->
            tx.execute(
                sql = "DELETE FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters()
            )
        }

        result shouldBe false
    }

    @Test
    fun `setter inn flere innslag`() = runTest {
        val items = listOf(
            TestEntity(string = "x1", integer = 1, enum = TestEnum.A, data1 = mapOf("key" to "t1")),
            TestEntity(string = "x2", integer = 2, enum = TestEnum.B, data1 = mapOf("key" to "t2")),
            TestEntity(string = "x3", integer = 3, enum = TestEnum.C, data1 = mapOf("key" to "t3")),
        )

        val result1 = transaction(testDataSource) { tx ->
            tx.batch(
                sql = """
                    INSERT INTO test (string, integer, enum, data_1)
                    VALUES (:string, :integer, :enum, :data_1)
                """.trimIndent(),
                items = items
            ) {
                it.toQueryParameters()
            }
        }

        result1.size shouldBe 3

        val result2 = transaction(testDataSource) { tx ->
            tx.queryList("SELECT * FROM test WHERE string LIKE 'x%'") {
                TestEntity(
                    id = it.long("id"),
                    string = it.string("string"),
                    integer = it.int("integer"),
                    enum = it.enum("enum"),
                    data1 = it.json("data_1"),
                )
            }
        }

        result2.size shouldBe 3
    }

    @Test
    fun `setter inn og henter null`() = runTest {
        val id = transaction(testDataSource, returnGeneratedKeys = true) { tx ->
            tx.updateAndReturnGeneratedKey(
                sql = """
                    INSERT INTO test (string, integer, enum, data_1, data_2)
                    VALUES ('test', 1, 'A', '{}', NULL)
                    RETURNING id
                """.trimIndent()
            )
        }

        id.shouldNotBeNull()
        id.shouldBePositive()

        val result = transaction(testDataSource) { tx ->
            tx.single(
                sql = "SELECT id, data_2 FROM test WHERE id = :id",
                queryParameters = id.toQueryParameters(),
            ) {
                mapOf(
                    "id" to it.long("id"),
                    "data_2" to it.jsonOrNull("data_2"),
                )
            }
        }

        result.shouldContain("id", id)
        result.shouldContain("data_2", null)
    }

    @Test
    fun `setter inn innslag og svarer med id`() = runTest {
        val id = transaction(testDataSource) { tx ->
            tx.query(
                sql = """
                    INSERT INTO test (string, integer, enum, data_1, data_2)
                    VALUES ('test', 1, 'A', '{}', NULL)
                    RETURNING id
                """.trimIndent()
            ) {
                it.long("id")
            }
        }
        id.shouldNotBeNull()
        id.shouldBePositive()
    }

    private suspend fun lagreEntity(): Long = transaction(testDataSource) { tx ->
        TestStore(tx).lagre(
            TestEntity(
                string = "string",
                integer = 1,
                enum = TestEnum.A,
                data1 = mapOf("key" to "value"),
            )
        )
    }

    private suspend fun lagreEntities(antall: Int): List<Long> = transaction(testDataSource) { tx ->
        TestStore(tx).lagre((1..antall).map {
            TestEntity(
                string = "string",
                integer = it,
                enum = TestEnum.A,
                data1 = mapOf("key" to "value"),
                data2 = null
            )
        })
    }
}
