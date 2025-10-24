package no.nav.hjelpemidler.database

import oracle.jdbc.OracleTypes
import java.sql.ResultSetMetaData

class OracleAdapter : DatabaseAdapter {
    override fun handle(row: Row, columnIndex: Int, columnType: Int, metaData: ResultSetMetaData): Any? {
        return when (columnType) {
            OracleTypes.TIMESTAMP -> row.localDateTimeOrNull(columnIndex)
            OracleTypes.TIMESTAMPTZ -> row.offsetDateTimeOrNull(columnIndex)
            // Fallback til JDBC-driver-definert mapping til Java
            else -> row.anyOrNull(columnIndex)
        }
    }
}
