package no.nav.hjelpemidler.database

import java.sql.ResultSetMetaData
import java.sql.Types

class PostgreSQLAdapter : DatabaseAdapter {
    override fun handle(row: Row, columnIndex: Int, columnType: Int, metaData: ResultSetMetaData): Any? {
        return when (columnType) {
            // Egendefinerte typer (i.e. CREATE TYPE)
            Types.STRUCT -> TODO("sqlType: $columnType ikke støttet")

            // JSON
            Types.OTHER -> when (metaData.getColumnTypeName(columnIndex)) {
                "json", "jsonb" -> row.treeOrNull(columnIndex)
                else -> row.anyOrNull(columnIndex)
            }

            // Fallback til JDBC-driver-definert mapping til Java
            else -> row.anyOrNull(columnIndex)
        }
    }
}
