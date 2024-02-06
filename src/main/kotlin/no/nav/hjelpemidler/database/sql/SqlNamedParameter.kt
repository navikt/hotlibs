package no.nav.hjelpemidler.database.sql

/**
 * @see [kotliquery.Query.regex]
 */
internal val regex = Regex("""(?<!:):(?!:)[a-zA-Z]\w+""")

@JvmInline
value class SqlNamedParameter internal constructor(private val value: String) : Comparable<SqlNamedParameter> {
    init {
        require(value.matches(regex)) { "Ugyldig parameter: $value" }
    }

    override fun compareTo(other: SqlNamedParameter): Int = value.compareTo(other.value)
    override fun toString(): String = value
}

fun sqlNamedParameterOf(value: String): SqlNamedParameter = SqlNamedParameter(value)
