package no.nav.hjelpemidler.database.postgresql

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.jdbc.getObject
import java.sql.ResultSet
import java.sql.Types
import java.time.Instant
import java.time.ZonedDateTime
import kotlin.reflect.KClass

internal class PostgreSQLRow(resultSet: ResultSet) : Row(resultSet) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? = when (type) {
        Instant::class -> instantOrNull(columnIndex) as T?
        ZonedDateTime::class -> zonedDateTimeOrNull(columnIndex) as T?
        else -> resultSet.getObject(columnIndex, type)
    }

    override fun asTree(columnIndex: Int): JsonNode {
        val metaData = resultSet.metaData
        val columnTypeName = metaData.getColumnTypeName(columnIndex)
        val value = when (val columnType = metaData.getColumnType(columnIndex)) {
            Types.DATE -> localDateOrNull(columnIndex)

            Types.TIME -> when (columnTypeName) {
                PostgreSQLTypeName.TIMETZ -> offsetTimeOrNull(columnIndex)
                else -> localTimeOrNull(columnIndex)
            }

            Types.TIMESTAMP -> when (columnTypeName) {
                PostgreSQLTypeName.TIMESTAMPTZ -> offsetDateTimeOrNull(columnIndex)
                else -> localDateTimeOrNull(columnIndex)
            }

            // Egendefinerte typer (i.e. CREATE TYPE)
            Types.STRUCT -> TODO("sqlType: $columnType ikke støttet")

            // JSON
            Types.OTHER -> when (columnTypeName) {
                PostgreSQLTypeName.JSON,
                PostgreSQLTypeName.JSONB,
                    -> treeOrNull(columnIndex)

                else -> anyOrNull(columnIndex)
            }

            else -> anyOrNull(columnIndex)
        }
        return node(value)
    }
}
