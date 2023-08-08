package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

typealias Row = kotliquery.Row

inline fun <reified T> Row.json(columnLabel: String): T =
    jsonMapper.readValue(string(columnLabel), jacksonTypeRef())

inline fun <reified T> Row.jsonOrNull(columnLabel: String): T? =
    stringOrNull(columnLabel)?.let {
        jsonMapper.readValue(it, jacksonTypeRef())
    }

inline fun <reified T : Enum<T>> Row.enum(columnLabel: String): T =
    enumValueOf(string((columnLabel)))

inline fun <reified T : Enum<T>> Row.enumOrNull(columnLabel: String): T? =
    stringOrNull(columnLabel)?.let<String, T>(::enumValueOf)

fun Row.toMap(): Map<String, Any?> {
    val metaData = checkNotNull(metaDataOrNull())
    return (1..metaData.columnCount).associate { columnIndex ->
        metaData.getColumnLabel(columnIndex) to anyOrNull(columnIndex)
    }
}
