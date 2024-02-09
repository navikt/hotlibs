package no.nav.hjelpemidler.database.sql

import org.intellij.lang.annotations.Language

class SqlBuilder internal constructor(
    private val baseSql: Sql,
    private val conditions: MutableSet<SqlCondition> = mutableSetOf(),
    private var orderBy: SqlOrderBy? = null,
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
        orderBy = SqlOrderBy(columnName, order)
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
            appendLine().append("WHERE TRUE")
            conditions.forEach(::appendCondition)
        }
        orderBy?.let(::appendOrderBy)
    }.let(::Sql)

    override fun toString(): String = toSql().toString()
}

internal fun StringBuilder.appendCondition(condition: SqlCondition): StringBuilder =
    appendLine().append('\t').append("AND ").append(condition)

internal fun StringBuilder.appendOrderBy(orderBy: SqlOrderBy): StringBuilder =
    appendLine().append(orderBy)

fun buildSql(@Language("PostgreSQL") baseSql: String, block: SqlBuilder.() -> Unit): Sql =
    buildSql(Sql(baseSql), block)

fun buildSql(baseSql: Sql, block: SqlBuilder.() -> Unit): Sql =
    SqlBuilder(baseSql).apply(block).toSql()
