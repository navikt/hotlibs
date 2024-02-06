package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

@JvmInline
value class SqlCondition internal constructor(@Language("PostgreSQL") private val value: String) :
    Comparable<SqlCondition> {
    infix fun and(other: SqlCondition): SqlCondition = SqlCondition("($value AND ${other.value})")

    infix fun or(other: SqlCondition): SqlCondition = SqlCondition("($value OR ${other.value})")
    operator fun not(): SqlCondition = SqlCondition("(NOT ($value))")

    override fun compareTo(other: SqlCondition): Int = value.compareTo(other.value)

    override fun toString(): String = value

    companion object {
        val TRUE = SqlCondition("TRUE")
        val FALSE = SqlCondition("FALSE")
    }
}

fun sqlConditionOf(@Language("PostgreSQL") value: String): SqlCondition = SqlCondition(value)
