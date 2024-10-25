package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SqlBuilderTest {
    @Test
    fun `Skal lage riktig SQL`() {
        val sql = buildSql("SELECT * FROM foobar") {
            WHERE("foo = 'bar'")
            WHERE("bar = 'foo'")
            WHERE(SqlCondition("x > 10") or SqlCondition("y < 10"))
            ORDER_BY("foo", SqlOrderBy.Order.DESC)
            ORDER_BY("bar")
        }
        sql.toString() shouldBe """
            SELECT * FROM foobar
            WHERE foo = 'bar' AND bar = 'foo' AND (x > 10 OR y < 10)
            ORDER BY foo DESC, bar
        """.trimIndent()
    }
}
