package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SqlConditionTest {
    @Test
    fun `SQL AND`() {
        (SqlCondition.TRUE AND SqlCondition.FALSE).toString() shouldBe "(TRUE AND FALSE)"
    }

    @Test
    fun `SQL OR`() {
        (SqlCondition.TRUE OR SqlCondition.FALSE).toString() shouldBe "(TRUE OR FALSE)"
    }

    @Test
    fun `SQL NOT`() {
        SqlCondition.TRUE.NOT().toString() shouldBe "(NOT (TRUE))"
    }
}
