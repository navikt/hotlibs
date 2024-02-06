package no.nav.hjelpemidler.database.sql

data class SqlOrderBy(
    val columnName: String,
    val order: Order = Order.ASC,
) {
    constructor(column: SqlColumn<*>, order: Order = Order.ASC) : this(column.columnName, order)

    override fun toString(): String = "ORDER BY $columnName $order"

    enum class Order { ASC, DESC }
}
