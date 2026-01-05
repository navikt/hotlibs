package no.nav.hjelpemidler.database.h2

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.Row
import java.sql.ResultSet
import kotlin.reflect.KClass

internal class H2Row(resultSet: ResultSet) : Row(resultSet) {
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? =
        nullable(resultSet.getObject(columnIndex, type.java))

    override fun asTree(columnIndex: Int): JsonNode = node(anyOrNull(columnIndex))
}
