package no.nav.hjelpemidler.database.oracle

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.jdbc.getObject
import oracle.jdbc.OracleTypes
import java.sql.ResultSet
import java.time.Instant
import kotlin.reflect.KClass

internal class OracleRow(resultSet: ResultSet) : Row(resultSet) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? = when (type) {
        Instant::class -> instantOrNull(columnIndex) as T?
        else -> resultSet.getObject(columnIndex, type)
    }

    override fun asTree(columnIndex: Int): JsonNode {
        val metaData = resultSet.metaData
        val value = when (metaData.getColumnType(columnIndex)) {
            OracleTypes.DATE -> localDateOrNull(columnIndex)
            OracleTypes.TIME -> localTimeOrNull(columnIndex)
            OracleTypes.TIMESTAMP -> localDateTimeOrNull(columnIndex)
            OracleTypes.TIMESTAMPTZ -> offsetDateTimeOrNull(columnIndex)
            else -> anyOrNull(columnIndex)
        }
        return node(value)
    }
}
