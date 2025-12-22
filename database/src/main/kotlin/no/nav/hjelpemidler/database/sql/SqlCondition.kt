package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

@Suppress("FunctionName")
@JvmInline
value class SqlCondition(@Language("SQL", prefix = "SELECT ") private val value: String) : Comparable<SqlCondition> {
    infix fun AND(other: SqlCondition): SqlCondition =
        SqlCondition("($value AND ${other.value})")

    infix fun OR(other: SqlCondition): SqlCondition =
        SqlCondition("($value OR ${other.value})")

    fun NOT(): SqlCondition =
        SqlCondition("(NOT ($value))")

    override fun compareTo(other: SqlCondition): Int =
        value.compareTo(other.value)

    override fun toString(): String = value

    companion object {
        val TRUE = SqlCondition("TRUE")
        val FALSE = SqlCondition("FALSE")
    }
}
