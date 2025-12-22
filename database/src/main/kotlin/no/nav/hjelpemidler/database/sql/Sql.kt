package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language
import java.util.stream.IntStream

@JvmInline
value class Sql(@Language("SQL") private val value: String) : Comparable<Sql>, CharSequence by value {
    fun replace(oldValue: String, newValue: String): Sql = Sql(value.replace(oldValue, newValue))

    override fun chars(): IntStream = value.chars()
    override fun codePoints(): IntStream = value.codePoints()
    override fun compareTo(other: Sql): Int = value.compareTo(other.value)
    override fun toString(): String = value
}
