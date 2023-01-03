package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.test.shouldBe
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.test.testTransaction
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class SessionExtensionsTest {
    @Test
    fun `henter alle rader`() {
        val rows = testTransaction { tx ->
            tx.queryList(sql = "SELECT * FROM person WHERE TRUE") {
                it.toMap()
            }
        }

        rows.size shouldBe 5
    }

    @Test
    fun `henter alle rader som page`() {
        val page = testTransaction { tx ->
            tx.queryPage(
                sql = "SELECT *, COUNT(*) OVER() AS total FROM person WHERE TRUE",
                queryParameters = emptyMap(),
                limit = 2,
                offset = 0
            ) {
                it.toMap()
            }
        }

        page.size shouldBe 2
        page.total shouldBe 5
    }

    @Test
    fun `henter første rad`() {
        val row = testTransaction { tx ->
            tx.query(
                sql = "SELECT * FROM person WHERE name = :name",
                queryParameters = mapOf("name" to "one")
            ) {
                it.toMap()
            }
        }

        assertNotNull(row)
        row["name"] shouldBe "one"
    }

    @Test
    fun `henter json`() {
        val row = testTransaction { tx ->
            tx.query(
                sql = "SELECT * FROM person WHERE name = :name",
                queryParameters = mapOf("name" to "two")
            ) {
                it.json<Map<String, Any?>>("data")
            }
        }

        assertNotNull(row)
        row["value"] shouldBe "two"
    }

    @Test
    fun `oppdaterer rad`() {
        val result = testTransaction { tx ->
            tx.update(
                sql = "UPDATE person SET age = 50 WHERE name = :name",
                queryParameters = mapOf("name" to "three")
            )
        }

        assertDoesNotThrow {
            result.validate()
        }
    }

    @Test
    fun `sletter rad`() {
        val result = testTransaction { tx ->
            tx.execute(
                sql = "DELETE FROM person WHERE name = :name",
                queryParameters = mapOf("name" to "four")
            )
        }

        result shouldBe false
    }

    @Test
    fun `batch insert`() {
        val dataSource = testDataSource()

        data class Person(
            val id: Long = -1,
            val name: String,
            val age: Int,
            val gender: Gender,
            val data: Map<String, Any?>,
        )

        val items = listOf(
            Person(name = "x1", age = 1, gender = Gender.FEMALE, data = mapOf("value" to "t1")),
            Person(name = "x2", age = 2, gender = Gender.FEMALE, data = mapOf("value" to "t2")),
            Person(name = "x3", age = 3, gender = Gender.MALE, data = mapOf("value" to "t3")),
        )

        val result1 = testTransaction(dataSource = dataSource) { tx ->
            tx.batch(
                sql = """
                    INSERT INTO person(name, age, gender, data)
                    VALUES (:name, :age, :gender, :data FORMAT JSON)
                """.trimIndent(),
                items = items
            ) {
                mapOf(
                    "name" to it.name,
                    "age" to it.age,
                    "gender" to it.gender.name,
                    "data" to jsonMapper.writeValueAsString(it.data),
                )
            }
        }

        result1.size shouldBe 3

        val result2 = testTransaction(dataSource = dataSource) { tx ->
            items.batch(
                session = tx,
                sql = """
                    INSERT INTO person(name, age, gender, data)
                    VALUES (:name, :age, :gender, :data FORMAT JSON)
                """.trimIndent(),
            ) {
                mapOf(
                    "name" to it.name,
                    "age" to it.age,
                    "gender" to it.gender.name,
                    "data" to jsonMapper.writeValueAsString(it.data),
                )
            }
        }

        result2.size shouldBe 3

        val savedItems = testTransaction(dataSource = dataSource) { tx ->
            tx.queryList("SELECT * FROM person WHERE name LIKE 'x%'") {
                Person(
                    id = it.long("id"),
                    name = it.string("name"),
                    age = it.int("age"),
                    gender = it.enum("gender"),
                    data = it.json("data"),
                )
            }
        }

        savedItems.size shouldBe 6
    }
}

enum class Gender {
    MALE, FEMALE
}
