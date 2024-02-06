package no.nav.hjelpemidler.database.sql

interface SqlColumn<T : Any> {
    val columnName: String

    fun isNull(): SqlCondition = SqlCondition("$columnName IS NULL")
    fun isNotNull(): SqlCondition = SqlCondition("$columnName IS NOT NULL")
    infix fun eq(value: T): SqlCondition = SqlCondition("$columnName = ${value.toQuotedString()}")
    infix fun ne(value: T): SqlCondition = SqlCondition("$columnName != ${value.toQuotedString()}")
    infix fun any(value: SqlNamedParameter) = SqlCondition("$columnName = ANY ($value)")
    infix fun none(value: SqlNamedParameter) = SqlCondition("$columnName != ANY ($value)")

    infix fun any(values: Collection<T>): SqlCondition =
        SqlCondition("$columnName IN (${values.joinToQuotedString()})")

    fun any(vararg values: T): SqlCondition = any(values.toList())

    infix fun none(values: Collection<T>): SqlCondition =
        SqlCondition("$columnName NOT IN (${values.joinToQuotedString()})")

    fun none(vararg values: T): SqlCondition = none(values.toList())

    infix fun jsonbExists(value: T): SqlCondition = SqlCondition("$columnName ?? '$value'")
}

private fun <T : Any> T.toQuotedString(): String = when (this) {
    is Boolean -> toString().uppercase()
    is CharSequence -> "'$this'"
    is Enum<*> -> "'$name'"
    is Number -> toString()
    is SqlNamedParameter -> toString()
    else -> error("'${this::class}' støttes ikke")
}

private fun <T : Any> Collection<T>.joinToQuotedString(): String = joinToString(", ") { it.toQuotedString() }
