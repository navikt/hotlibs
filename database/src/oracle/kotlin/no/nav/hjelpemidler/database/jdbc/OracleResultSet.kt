package no.nav.hjelpemidler.database.jdbc

import com.fasterxml.jackson.databind.JsonNode
import oracle.jdbc.OracleTypes
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.OffsetDateTime

internal class OracleResultSet(resultSet: ResultSet) : AbstractResultSetAdapter(resultSet) {
    override fun asTree(columnIndex: Int): JsonNode {
        val value = when (metaData.getColumnType(columnIndex)) {
            OracleTypes.TIMESTAMP -> valueOrNull<LocalDateTime>(columnIndex)
            OracleTypes.TIMESTAMPTZ -> valueOrNull<OffsetDateTime>(columnIndex)
            else -> return super.asTree(columnIndex)
        }
        return node(value)
    }
}
