package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SqlConditionTest {
    @Test
    fun `SQL AND`() {
        (SqlCondition.TRUE and SqlCondition.FALSE).toString() shouldBe "(TRUE AND FALSE)"
    }

    @Test
    fun `SQL OR`() {
        (SqlCondition.TRUE or SqlCondition.FALSE).toString() shouldBe "(TRUE OR FALSE)"
    }

    @Test
    fun `SQL NOT`() {
        SqlCondition.TRUE.not().toString() shouldBe "(NOT (TRUE))"
    }
}
