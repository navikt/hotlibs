package no.nav.hjelpemidler.database.jdbc

import com.fasterxml.jackson.databind.JsonNode
import java.sql.ResultSet
import kotlin.reflect.KClass

interface ResultSetAdapter : ResultSet {
    /**
     * Konverter kolonnen til [JsonNode].
     */
    fun asTree(columnIndex: Int): JsonNode

    fun <T : Any> valueOrNull(columnIndex: Int, type: KClass<T>): T?
    fun <T : Any> valueOrNull(columnLabel: String, type: KClass<T>): T?
}
