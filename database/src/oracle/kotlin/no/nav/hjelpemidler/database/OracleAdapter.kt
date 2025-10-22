package no.nav.hjelpemidler.database

import java.sql.ResultSetMetaData

class OracleAdapter : DatabaseAdapter {
    override fun handle(row: Row, columnIndex: Int, columnType: Int, metaData: ResultSetMetaData): Any? {
        return when (columnType) {
            // Fallback til JDBC-driver-definert mapping til Java
            else -> row.anyOrNull(columnIndex)
        }
    }
}
