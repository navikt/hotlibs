package no.nav.hjelpemidler.database.sql

import no.nav.hjelpemidler.database.sql.SqlOrderBy.Order
import org.intellij.lang.annotations.Language

class SqlBuilder internal constructor(
    private val value: Sql,
    private val conditions: MutableSet<SqlCondition> = mutableSetOf(),
    private var orderBy: SqlOrderBy? = null,
) {
    fun orderBy(columnName: String, order: Order = Order.ASC): SqlBuilder {
        orderBy = SqlOrderBy(columnName, order)
        return this
    }

    fun orderBy(column: SqlColumn<*>, order: Order = Order.ASC): SqlBuilder =
        orderBy(column.columnName, order)

    fun orderBy(orderBy: SqlOrderBy?): SqlBuilder =
        if (orderBy == null) this else orderBy(orderBy.columnName, orderBy.order)

    fun filter(condition: SqlCondition): SqlBuilder {
        conditions.add(condition)
        return this
    }

    fun filter(filter: SqlFilter?): SqlBuilder {
        val condition = filter?.condition ?: return this
        return filter(condition)
    }

    fun copy(): SqlBuilder = SqlBuilder(value, conditions.toMutableSet(), orderBy)

    fun toSql(): Sql = buildString {
        append(value)
        if (conditions.isNotEmpty()) {
            appendLine().append("WHERE TRUE")
            conditions.forEach(::appendCondition)
        }
        orderBy?.let(::appendOrderBy)
        appendLine()
    }.let(::sqlOf)

    override fun toString(): String = toSql().toString()
}

internal fun StringBuilder.appendCondition(condition: SqlCondition): StringBuilder =
    appendLine().append('\t').append("AND ").append(condition)

internal fun StringBuilder.appendOrderBy(orderBy: SqlOrderBy): StringBuilder =
    appendLine().append(orderBy)

fun buildSql(@Language("PostgreSQL") sql: String, block: SqlBuilder.() -> Unit): Sql =
    buildSql(sqlOf(sql), block)

fun buildSql(sql: Sql, block: SqlBuilder.() -> Unit): Sql =
    SqlBuilder(sql).apply(block).toSql()
