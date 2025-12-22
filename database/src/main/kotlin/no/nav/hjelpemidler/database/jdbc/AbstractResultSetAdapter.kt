package no.nav.hjelpemidler.database.jdbc

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.node
import java.sql.ResultSet
import java.sql.SQLType
import java.sql.Types
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import kotlin.reflect.KClass

abstract class AbstractResultSetAdapter(private val resultSet: ResultSet) : ResultSetAdapter, ResultSet by resultSet {
    override fun asTree(columnIndex: Int): JsonNode {
        val value = when (metaData.getColumnType(columnIndex)) {
            Types.ARRAY -> nullable(getArray(columnIndex))?.array
            Types.CLOB -> stringOrNull(columnIndex)
            Types.DATE -> valueOrNull<LocalDate>(columnIndex)
            // Fallback til databasespesifikk håndtering av kolonne
            else -> nullable(getObject(columnIndex))
        }
        return node(value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? = when (type) {
        Instant::class -> valueOrNull<OffsetDateTime>(columnIndex)?.toInstant()
        ZonedDateTime::class -> valueOrNull<OffsetDateTime>(columnIndex)?.toZonedDateTime()
        else -> nullable(getObject(columnIndex, type.java))
    } as T?

    override fun <T : Any> valueOrNull(columnLabel: String, type: KClass<T>): T? =
        valueOrNull(findColumn(columnLabel), type)

    protected inline fun <reified T> valueOrNull(columnIndex: Int): T? = when (val type = T::class) {
        Instant::class -> nullable(getObject(columnIndex, OffsetDateTime::class.java))?.toInstant()
        ZonedDateTime::class -> nullable(getObject(columnIndex, OffsetDateTime::class.java))?.toZonedDateTime()
        else -> nullable(getObject(columnIndex, type.java))
    } as T?

    protected inline fun <reified T> valueOrNull(columnLabel: String): T? =
        valueOrNull<T>(findColumn(columnLabel))

    protected fun stringOrNull(columnIndex: Int): String? = nullable(getString(columnIndex))

    protected fun <T> nullable(value: T): T? = if (wasNull()) null else value

    protected fun node(value: Any?): JsonNode = value as? JsonNode ?: jsonMapper.nodeFactory.node(value)

    override fun updateObject(columnIndex: Int, value: Any?, targetSqlType: SQLType, scaleOrLength: Int) =
        resultSet.updateObject(columnIndex, value, targetSqlType, scaleOrLength)

    override fun updateObject(columnLabel: String, value: Any?, targetSqlType: SQLType, scaleOrLength: Int) =
        resultSet.updateObject(columnLabel, value, targetSqlType, scaleOrLength)

    override fun updateObject(columnIndex: Int, value: Any?, targetSqlType: SQLType) =
        resultSet.updateObject(columnIndex, value, targetSqlType)

    override fun updateObject(columnLabel: String, value: Any?, targetSqlType: SQLType) =
        resultSet.updateObject(columnLabel, value, targetSqlType)
}

internal class GenericResultSet(resultSet: ResultSet) : AbstractResultSetAdapter(resultSet)
