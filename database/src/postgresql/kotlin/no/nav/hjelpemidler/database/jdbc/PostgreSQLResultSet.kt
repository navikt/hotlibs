package no.nav.hjelpemidler.database.jdbc

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.serialization.jackson.jsonToTreeOrNull
import java.sql.ResultSet
import java.sql.Types
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime

internal class PostgreSQLResultSet(resultSet: ResultSet) : AbstractResultSetAdapter(resultSet) {
    override fun asTree(columnIndex: Int): JsonNode {
        val columnTypeName = metaData.getColumnTypeName(columnIndex)
        val value = when (val columnType = metaData.getColumnType(columnIndex)) {
            Types.TIME -> when (columnTypeName) {
                PostgreSQLTypeName.TIMETZ -> valueOrNull<OffsetTime>(columnIndex)
                else -> valueOrNull<LocalTime>(columnIndex)
            }

            Types.TIMESTAMP -> when (columnTypeName) {
                PostgreSQLTypeName.TIMESTAMPTZ -> valueOrNull<OffsetDateTime>(columnIndex)
                else -> valueOrNull<LocalDateTime>(columnIndex)
            }

            // Egendefinerte typer (i.e. CREATE TYPE)
            Types.STRUCT -> TODO("sqlType: $columnType ikke støttet")

            // JSON
            Types.OTHER -> when (columnTypeName) {
                PostgreSQLTypeName.JSON,
                PostgreSQLTypeName.JSONB,
                    -> stringOrNull(columnIndex)?.let { jsonToTreeOrNull(it) }

                else -> return super.asTree(columnIndex)
            }

            else -> return super.asTree(columnIndex)
        }
        return node(value)
    }
}

internal object PostgreSQLTypeName {
    const val JSON = "json"
    const val JSONB = "jsonb"
    const val TIMETZ = "timetz"
    const val TIMESTAMPTZ = "timestamptz"
}
