package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

@JvmInline
value class Sql(@Language("SQL") private val value: String) : Comparable<Sql>, CharSequence by value {
    fun replace(oldValue: String, newValue: String): Sql = Sql(value.replace(oldValue, newValue))

    override fun compareTo(other: Sql): Int = value.compareTo(other.value)
    override fun toString(): String = value
}
