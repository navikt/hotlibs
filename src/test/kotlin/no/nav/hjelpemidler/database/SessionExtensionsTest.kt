package no.nav.hjelpemidler.database

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.Test1Entity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestStoreContext
import kotlin.test.Test

internal class SessionExtensionsTest {
    private val storeContext = TestStoreContext()

    @Test
    fun `henter ett innslag`() = runTest {
        val id = lagreEntity()
        val sql = "SELECT id FROM test_1 WHERE id = :id"
        val queryParameters = id.toQueryParameters()

        transaction(storeContext.dataSource) { tx ->
            tx.query(sql = sql, queryParameters = queryParameters) { row ->
                row.long("id")
            }
        } shouldBe id

        transaction(storeContext.dataSource) { tx ->
            tx.query(sql = sql, queryParameters = 0.toQueryParameters()) { row ->
                row.long("id")
            }
        } shouldBe null

        shouldNotThrow<NoSuchElementException> {
            transaction(storeContext.dataSource) { tx ->
                tx.single(sql = sql, queryParameters = queryParameters) { row ->
                    row.toMap()
                }
            }
        }
    }

    @Test
    fun `henter flere innslag`() = runTest {
        val ids = lagreEntities(10)
        val result = transaction(storeContext.dataSource) { tx ->
            tx.queryList(
                sql = "SELECT * FROM test_1 WHERE id = ANY(:ids)",
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
        val ids = lagreEntities(10)
        val result = transaction(storeContext.dataSource) { tx ->
            tx.queryPage(
                sql = """
                    SELECT *, COUNT(1) OVER() AS total
                    FROM test_1
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
        val result = transaction(storeContext.dataSource) { tx ->
            tx.single(
                sql = "SELECT data_1 FROM test_1 WHERE id = :id",
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
        val result = transaction(storeContext.dataSource) { tx ->
            tx.update(
                sql = "UPDATE test_1 SET integer = 50 WHERE id = :id",
                queryParameters = id.toQueryParameters()
            )
        }

        shouldNotThrowAny {
            result.expect(1)
        }
    }

    @Test
    fun `sletter innslag`() = runTest {
        val id = lagreEntity()
        val result = transaction(storeContext.dataSource) { tx ->
            tx.execute(
                sql = "DELETE FROM test_1 WHERE id = :id",
                queryParameters = id.toQueryParameters()
            )
        }

        result shouldBe false
    }

    @Test
    fun `setter inn flere innslag`() = runTest {
        val items = listOf(
            Test1Entity(string = "x1", integer = 1, enum = TestEnum.A, data1 = mapOf("key" to "t1")),
            Test1Entity(string = "x2", integer = 2, enum = TestEnum.B, data1 = mapOf("key" to "t2")),
            Test1Entity(string = "x3", integer = 3, enum = TestEnum.C, data1 = mapOf("key" to "t3")),
        )

        val result1 = transaction(storeContext.dataSource) { tx ->
            tx.batch(
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1)
                    VALUES (:string, :integer, :enum, :data_1)
                """.trimIndent(),
                items = items
            ) {
                it.toQueryParameters()
            }
        }

        result1.size shouldBe 3

        val result2 = transaction(storeContext.dataSource) { tx ->
            tx.queryList("SELECT * FROM test_1 WHERE string LIKE 'x%'") {
                Test1Entity(
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
        val id = transaction(storeContext.dataSource, returnGeneratedKey = true) { tx ->
            tx.updateAndReturnGeneratedKey(
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1, data_2)
                    VALUES ('test', 1, 'A', '{}', NULL)
                    RETURNING id
                """.trimIndent()
            )
        }

        id.shouldNotBeNull()
        id.shouldBePositive()

        val result = transaction(storeContext.dataSource) { tx ->
            tx.single(
                sql = "SELECT id, data_2 FROM test_1 WHERE id = :id",
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
        val id = transaction(storeContext.dataSource) { tx ->
            tx.query(
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1, data_2)
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

    private suspend fun lagreEntity(): Long =
        transaction(storeContext) { ctx ->
            ctx.testStore.lagre(
                Test1Entity(
                    string = "string",
                    integer = 1,
                    enum = TestEnum.A,
                    data1 = mapOf("key" to "value"),
                )
            )
        }

    private suspend fun lagreEntities(antall: Int): List<Long> =
        transaction(storeContext) { ctx ->
            ctx.testStore.lagre((1..antall).map {
                Test1Entity(
                    string = "string",
                    integer = it,
                    enum = TestEnum.A,
                    data1 = mapOf("key" to "value"),
                    data2 = null
                )
            })
        }
}
