package no.nav.hjelpemidler.database.sql

import no.nav.hjelpemidler.collections.OrderBy.Direction
import org.intellij.lang.annotations.Language

/**
 * Inspirert av MyBatis SQL Builder.
 *
 * @see <a href="https://mybatis.org/mybatis-3/statement-builders.html">The SQL Builder Class</a>
 */
@Suppress("FunctionName")
class SqlBuilder internal constructor(
    private val baseSql: Sql,
    private val conditions: MutableSet<SqlCondition> = mutableSetOf(),
    private var orderBy: MutableSet<SqlOrderBy> = mutableSetOf(),
) {
    fun WHERE(condition: SqlCondition) {
        conditions.add(condition)
    }

    fun WHERE(@Language("SQL", prefix = "SELECT ") condition: String) =
        WHERE(SqlCondition(condition))

    fun WHERE(filter: SqlFilter?) {
        val condition = filter?.condition ?: return
        WHERE(condition)
    }

    fun ORDER_BY(columnName: String, direction: Direction = Direction.ASCENDING) {
        if (direction != Direction.NONE) {
            orderBy.add(SqlOrderBy(columnName, direction))
        }
    }

    fun ORDER_BY(column: SqlColumn, direction: Direction = Direction.ASCENDING) =
        ORDER_BY(column.columnName, direction)

    fun ORDER_BY(orderBy: SqlOrderBy?) {
        val (columnName, direction) = orderBy ?: return
        ORDER_BY(columnName, direction)
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

fun buildSql(@Language("SQL") baseSql: String, block: SqlBuilder.() -> Unit): Sql =
    buildSql(Sql(baseSql), block)

fun buildSql(baseSql: Sql, block: SqlBuilder.() -> Unit): Sql =
    SqlBuilder(baseSql).apply(block).toSql()
