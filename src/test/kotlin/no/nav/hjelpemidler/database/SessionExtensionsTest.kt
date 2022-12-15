package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.test.shouldBe
import no.nav.hjelpemidler.database.test.testTransaction
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class SessionExtensionsTest {
    @Test
    fun `henter alle rader`() {
        val rows = testTransaction { tx ->
            tx.queryList(sql = "SELECT * FROM test WHERE TRUE") {
                it.toMap()
            }
        }

        rows.size shouldBe 5
    }

    @Test
    fun `henter alle rader som page`() {
        val page = testTransaction { tx ->
            tx.queryPage(
                sql = "SELECT *, COUNT(*) OVER() AS total FROM test WHERE TRUE",
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
                sql = "SELECT * FROM test WHERE name = :name",
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
                sql = "SELECT * FROM test WHERE name = :name",
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
                sql = "UPDATE test SET age = 50 WHERE name = :name",
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
                sql = "DELETE FROM test WHERE name = :name",
                queryParameters = mapOf("name" to "four")
            )
        }

        result shouldBe false
    }
}
