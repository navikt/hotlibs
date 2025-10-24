package no.nav.hjelpemidler.database

import java.sql.ResultSetMetaData
import java.sql.Types

class PostgreSQLAdapter : DatabaseAdapter {
    override fun handle(row: Row, columnIndex: Int, columnType: Int, metaData: ResultSetMetaData): Any? {
        return when (columnType) {
            Types.TIME -> when (metaData.getColumnTypeName(columnIndex)) {
                PostgreSQLTypeName.TIMETZ -> row.offsetTimeOrNull(columnIndex)
                else -> row.localTimeOrNull(columnIndex)
            }

            Types.TIMESTAMP -> when (metaData.getColumnTypeName(columnIndex)) {
                PostgreSQLTypeName.TIMESTAMPTZ -> row.offsetDateTimeOrNull(columnIndex)
                else -> row.localDateTimeOrNull(columnIndex)
            }

            // Egendefinerte typer (i.e. CREATE TYPE)
            Types.STRUCT -> TODO("sqlType: $columnType ikke støttet")

            // JSON
            Types.OTHER -> when (metaData.getColumnTypeName(columnIndex)) {
                PostgreSQLTypeName.JSON, PostgreSQLTypeName.JSONB -> row.treeOrNull(columnIndex)
                else -> row.anyOrNull(columnIndex)
            }

            // Fallback til JDBC-driver-definert mapping til Java
            else -> row.anyOrNull(columnIndex)
        }
    }
}

internal object PostgreSQLTypeName {
    const val JSON = "json"
    const val JSONB = "jsonb"
    const val TIMETZ = "timetz"
    const val TIMESTAMPTZ = "timestamptz"
}
