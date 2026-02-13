package no.nav.hjelpemidler.database.sql

import no.nav.hjelpemidler.collections.OrderBy
import no.nav.hjelpemidler.collections.OrderBy.Direction

data class SqlOrderBy(
    val columnName: String,
    override val direction: Direction = Direction.ASCENDING,
) : OrderBy<String> {
    constructor(column: SqlColumn, direction: Direction = Direction.ASCENDING) : this(column.columnName, direction)

    override val property: String
        get() = columnName

    override fun toString(): String = when (direction) {
        Direction.ASCENDING -> columnName
        Direction.ASCENDING_NULLS_FIRST -> "$columnName NULLS FIRST"
        Direction.DESCENDING -> "$columnName DESC"
        Direction.DESCENDING_NULLS_LAST -> "$columnName DESC NULLS LAST"
        Direction.NONE -> ""
    }
}
