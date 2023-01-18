package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import kotliquery.Row
import java.sql.Types
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

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

inline fun <reified T : Any> Row.to(): T {
    check(T::class.isData)
    val constructor = checkNotNull(T::class.primaryConstructor)
    val metaData = checkNotNull(metaDataOrNull())
    val columnTypeByColumnLabel = (1..metaData.columnCount).associate { columnIndex ->
        metaData.getColumnLabel(columnIndex) to metaData.getColumnType(columnIndex)
    }
    val fromValue = constructor.valueParameters.associate { parameter ->
        val columnLabel = when (val column = parameter.findAnnotation<Column>()) {
            null -> checkNotNull(parameter.name)
            else -> column.name
        }
        columnLabel to when (columnTypeByColumnLabel[columnLabel]) {
            Types.OTHER -> jsonOrNull(columnLabel)
            else -> anyOrNull(columnLabel)
        }
    }
    return jsonMapper.convertValue(fromValue, jacksonTypeRef())
}
