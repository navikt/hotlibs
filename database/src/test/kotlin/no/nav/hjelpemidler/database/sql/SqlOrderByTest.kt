package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.collections.OrderBy
import kotlin.test.Test

class SqlOrderByTest {
    @Test
    fun `Skal lage riktig SQL`() {
        SqlOrderBy("foobar", OrderBy.Direction.ASCENDING).toString() shouldBe "foobar"
        SqlOrderBy("foobar", OrderBy.Direction.ASCENDING_NULLS_FIRST).toString() shouldBe "foobar NULLS FIRST"
        SqlOrderBy("foobar", OrderBy.Direction.DESCENDING).toString() shouldBe "foobar DESC"
        SqlOrderBy("foobar", OrderBy.Direction.DESCENDING_NULLS_LAST).toString() shouldBe "foobar DESC NULLS LAST"
    }
}
