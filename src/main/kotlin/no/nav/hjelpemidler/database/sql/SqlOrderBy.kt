package no.nav.hjelpemidler.database.sql

data class SqlOrderBy(val columnName: String, val order: Order = Order.ASC) {
    constructor(column: SqlColumn, order: Order = Order.ASC) : this(column.columnName, order)

    override fun toString(): String = when (order) {
        Order.ASC -> columnName
        Order.DESC -> "$columnName DESC"
        Order.ASC_NULLS_FIRST -> "$columnName NULLS FIRST"
        Order.DESC_NULLS_LAST -> "$columnName DESC NULLS LAST"
    }

    enum class Order { ASC, DESC, ASC_NULLS_FIRST, DESC_NULLS_LAST }
}
