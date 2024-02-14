package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SqlOrderByTest {
    @Test
    fun `Skal lage riktig SQL`() {
        SqlOrderBy("foobar", SqlOrderBy.Order.ASC).toString() shouldBe "foobar"
        SqlOrderBy("foobar", SqlOrderBy.Order.DESC).toString() shouldBe "foobar DESC"
        SqlOrderBy("foobar", SqlOrderBy.Order.ASC_NULLS_FIRST).toString() shouldBe "foobar NULLS FIRST"
        SqlOrderBy("foobar", SqlOrderBy.Order.DESC_NULLS_LAST).toString() shouldBe "foobar DESC NULLS LAST"
    }
}
