package no.nav.hjelpemidler.database.h2

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.jdbc.getObject
import java.sql.ResultSet
import kotlin.reflect.KClass

internal class H2Row(resultSet: ResultSet) : Row(resultSet) {
    override fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T? = resultSet.getObject(columnIndex, type)

    override fun asTree(columnIndex: Int): JsonNode = node(anyOrNull(columnIndex))
}
