package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SqlConditionTest {
    @Test
    fun `SQL AND`() {
        val condition = SqlCondition.TRUE and SqlCondition.FALSE
        condition.toString() shouldBe "(TRUE AND FALSE)"
    }

    @Test
    fun `SQL OR`() {
        val condition = SqlCondition.TRUE or SqlCondition.FALSE
        condition.toString() shouldBe "(TRUE OR FALSE)"
    }

    @Test
    fun `SQL NOT`() {
        val condition = SqlCondition.TRUE.not()
        condition.toString() shouldBe "(NOT (TRUE))"
    }
}
