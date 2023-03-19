package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.test.AbstractDatabaseTest
import no.nav.hjelpemidler.database.test.Test1Entity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class SessionExtensionsTest : AbstractDatabaseTest() {
    @Test
    fun `henter alle innslag som map`() {
        val result = testTransaction { tx ->
            tx.queryList(sql = "SELECT * FROM test_1 WHERE TRUE") {
                it.toMap()
            }
        }

        result.size shouldBe 5
    }

    @Test
    fun `henter side`() {
        val result = testTransaction { tx ->
            tx.queryPage(
                sql = "SELECT *, COUNT(1) OVER() AS total FROM test_1 WHERE TRUE",
                limit = 2,
                offset = 0
            ) {
                it.toMap()
            }
        }

        result.size shouldBe 2
        result.total shouldBe 5
    }

    @Test
    fun `henter innslag`() {
        val result = testTransaction { tx ->
            tx.single(
                sql = "SELECT * FROM test_1 WHERE string = :string",
                queryParameters = mapOf("string" to "string2")
            ) {
                it.toMap()
            }
        }

        assertNotNull(result)
        result["string"] shouldBe "string2"
    }

    @Test
    fun `henter json`() {
        val result = testTransaction { tx ->
            tx.query(
                sql = "SELECT * FROM test_1 WHERE string = :string",
                queryParameters = mapOf("string" to "string2")
            ) {
                it.json<Map<String, Any?>>("data_1")
            }
        }

        assertNotNull(result)
        result["key"] shouldBe "value2"
    }

    @Test
    fun `oppdaterer innslag`() {
        val result = testTransaction { tx ->
            tx.update(
                sql = "UPDATE test_1 SET integer = 50 WHERE string = :string",
                queryParameters = mapOf("string" to "string3")
            )
        }

        assertDoesNotThrow {
            result.expect(1)
        }
    }

    @Test
    fun `sletter innslag`() {
        val result = testTransaction { tx ->
            tx.execute(
                sql = "DELETE FROM test_1 WHERE string = :string",
                queryParameters = mapOf("string" to "string4")
            )
        }

        result shouldBe false
    }

    @Test
    fun `setter inn flere innslag`() {
        val items = listOf(
            Test1Entity(string = "x1", integer = 1, enum = TestEnum.A, data1 = mapOf("key" to "t1")),
            Test1Entity(string = "x2", integer = 2, enum = TestEnum.B, data1 = mapOf("key" to "t2")),
            Test1Entity(string = "x3", integer = 3, enum = TestEnum.C, data1 = mapOf("key" to "t3")),
        )

        val result1 = testTransaction { tx ->
            tx.batch(
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1)
                    VALUES (:string, :integer, :enum, :data1)
                """.trimIndent(),
                items = items
            ) {
                mapOf(
                    "string" to it.string,
                    "integer" to it.integer,
                    "enum" to it.enum.name,
                    "data1" to pgJsonbOf(it.data1),
                )
            }
        }

        result1.size shouldBe 3

        val result2 = testTransaction { tx ->
            tx.batch(
                items = items,
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1)
                    VALUES (:string, :integer, :enum, :data1)
                """.trimIndent(),
            ) {
                mapOf(
                    "string" to it.string,
                    "integer" to it.integer,
                    "enum" to it.enum.name,
                    "data1" to pgJsonbOf(it.data1),
                )
            }
        }

        result2.size shouldBe 3

        val savedItems = testTransaction { tx ->
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

        savedItems.size shouldBe 6
    }

    @Test
    fun `setter inn og henter null`() {
        val id = testTransaction(returnGeneratedKey = true) { tx ->
            tx.updateAndReturnGeneratedKey(
                sql = """
                    INSERT INTO test_1(string, integer, enum, data_1, data_2)
                    VALUES ('test', 1, 'A', '{}', NULL)
                    RETURNING id
                """.trimIndent()
            )
        }
        assertNotNull(id)
        val result = testTransaction { tx ->
            tx.single("SELECT id, data_2 FROM test_1 WHERE id = :id", mapOf("id" to id)) {
                mapOf(
                    "id" to it.long("id"),
                    "data2" to it.jsonOrNull("data_2"),
                )
            }
        }
        result["id"] shouldBe id
        result["data2"] shouldBe null
    }

    @Test
    fun `setter inn innslag og svarer med id`() {
        val id = testTransaction { tx ->
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
        assertNotNull(id)
    }
}
