package no.nav.hjelpemidler.database

import com.fasterxml.jackson.databind.JsonNode
import java.sql.ResultSet
import kotlin.reflect.KClass

internal class GenericRow(resultSet: ResultSet) : Row(resultSet) {
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? =
        nullable(resultSet.getObject(columnIndex, type.java))

    override fun asTree(columnIndex: Int): JsonNode = node(anyOrNull(columnIndex))
}
