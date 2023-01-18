package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.test.AbstractDatabaseTest
import no.nav.hjelpemidler.database.test.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class SessionExtensionsTest : AbstractDatabaseTest() {
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
    fun `henter alle rader med reflection`() {
        val rows = testTransaction { tx ->
            tx.queryList(sql = "SELECT * FROM person WHERE TRUE LIMIT 1") {
                it.to<Person>()
            }
        }

        rows.size shouldBe 1
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
            tx.single(
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
            result.expect(1)
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
        val items = listOf(
            Person(name = "x1", age = 1, gender = Gender.FEMALE, data = mapOf("value" to "t1")),
            Person(name = "x2", age = 2, gender = Gender.FEMALE, data = mapOf("value" to "t2")),
            Person(name = "x3", age = 3, gender = Gender.MALE, data = mapOf("value" to "t3")),
        )

        val result1 = testTransaction { tx ->
            tx.batch(
                sql = """
                    INSERT INTO person(name, age, gender, data)
                    VALUES (:name, :age, :gender, :data)
                """.trimIndent(),
                items = items
            ) {
                mapOf(
                    "name" to it.name,
                    "age" to it.age,
                    "gender" to it.gender.name,
                    "data" to pgJsonbOf(it.data),
                )
            }
        }

        result1.size shouldBe 3

        val result2 = testTransaction { tx ->
            items.batch(
                session = tx,
                sql = """
                    INSERT INTO person(name, age, gender, data)
                    VALUES (:name, :age, :gender, :data)
                """.trimIndent(),
            ) {
                mapOf(
                    "name" to it.name,
                    "age" to it.age,
                    "gender" to it.gender.name,
                    "data" to pgJsonbOf(it.data),
                )
            }
        }

        result2.size shouldBe 3

        val savedItems = testTransaction { tx ->
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

    @Test
    fun `lagrer og henter null fra JSON-kolonne`() {
        val id = testTransaction(returnGeneratedKey = true) { tx ->
            tx.updateAndReturnGeneratedKey("INSERT INTO json(data) VALUES (NULL)")
        }
        assertNotNull(id)
        val json = testTransaction { tx ->
            tx.single("SELECT id, data FROM json") {
                mapOf(
                    "id" to it.long("id"),
                    "data" to it.jsonOrNull("data"),
                )
            }
        }
        json["id"] shouldBe id
        json["data"] shouldBe null
    }

    @Test
    fun `setter inn rad med query`() {
        val id = testTransaction { tx ->
            tx.query("INSERT INTO json(data) VALUES (NULL) RETURNING id") {
                it.long("id")
            }
        }
        assertNotNull(id)
    }
}

data class Person(
    @Column("id")
    val id: Long = -1,
    @Column("name")
    val name: String,
    @Column("age")
    val age: Int,
    @Column("gender")
    val gender: Gender,
    @Column("data")
    val data: Map<String, Any?>,
) {
    val foo: String = "foo"
    val bar: String get() = "bar"
}

enum class Gender {
    MALE, FEMALE
}
