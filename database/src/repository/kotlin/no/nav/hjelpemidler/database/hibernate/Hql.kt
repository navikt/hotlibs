package no.nav.hjelpemidler.database.hibernate

import org.intellij.lang.annotations.Language

@JvmInline
value class Hql(@param:Language("HQL") private val value: String) : Comparable<Hql>, CharSequence by value {
    fun replace(oldValue: String, newValue: String): Hql = Hql(value.replace(oldValue, newValue))

    override fun compareTo(other: Hql): Int = value.compareTo(other.value)
    override fun toString(): String = value
}
