package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

class SqlBuilder internal constructor(
    private val baseSql: Sql,
    private val conditions: MutableSet<SqlCondition> = mutableSetOf(),
    private var orderBy: MutableSet<SqlOrderBy> = mutableSetOf()
) {
    fun filter(condition: SqlCondition) {
        conditions.add(condition)
    }

    fun filter(@Language("PostgreSQL") condition: String) = filter(SqlCondition(condition))

    fun filter(filter: SqlFilter?) {
        val condition = filter?.condition ?: return
        filter(condition)
    }

    fun orderBy(columnName: String, order: SqlOrderBy.Order = SqlOrderBy.Order.ASC) {
        orderBy.add(SqlOrderBy(columnName, order))
    }

    fun orderBy(column: SqlColumn, order: SqlOrderBy.Order = SqlOrderBy.Order.ASC) = orderBy(column.columnName, order)

    fun orderBy(orderBy: SqlOrderBy?) {
        val (columnName, order) = orderBy ?: return
        orderBy(columnName, order)
    }

    fun copy(): SqlBuilder = SqlBuilder(baseSql, conditions.toMutableSet(), orderBy)

    fun toSql(): Sql = buildString {
        append(baseSql)
        if (conditions.isNotEmpty()) {
            conditions.joinTo(buffer = this, separator = " AND ", prefix = "\nWHERE ")
        }
        if (orderBy.isNotEmpty()) {
            orderBy.joinTo(buffer = this, separator = ", ", prefix = "\nORDER BY ")
        }
    }.let(::Sql)

    override fun toString(): String = toSql().toString()
}

fun buildSql(@Language("PostgreSQL") baseSql: String, block: SqlBuilder.() -> Unit): Sql =
    buildSql(Sql(baseSql), block)

fun buildSql(baseSql: Sql, block: SqlBuilder.() -> Unit): Sql =
    SqlBuilder(baseSql).apply(block).toSql()
