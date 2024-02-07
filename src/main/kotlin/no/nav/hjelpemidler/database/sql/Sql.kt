package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

@JvmInline
value class Sql(@Language("PostgreSQL") private val value: String) : Comparable<Sql> {
    override fun compareTo(other: Sql): Int = value.compareTo(other.value)
    override fun toString(): String = value
}
