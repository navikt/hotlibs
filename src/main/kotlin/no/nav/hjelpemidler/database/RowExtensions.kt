package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import kotliquery.Row
import java.sql.Types

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

inline fun <reified T : Any> Row.receive(): T {
    val schema = T::class.schema()
    val metaData = checkNotNull(metaDataOrNull())
    val columnTypeByColumnLabel = (1..metaData.columnCount).associate { columnIndex ->
        metaData.getColumnLabel(columnIndex) to metaData.getColumnType(columnIndex)
    }
    val columnTypeNameByColumnLabel = (1..metaData.columnCount).associate { columnIndex ->
        metaData.getColumnLabel(columnIndex) to metaData.getColumnTypeName(columnIndex)
    }
    val fromValue = schema.columns.associate { column ->
        val columnType = columnTypeByColumnLabel[column.columnName]
        val columnTypeName = columnTypeNameByColumnLabel[column.columnName]
        column.parameterName to when (columnType) {
            Types.OTHER -> jsonOrNull(column.columnAlias)
            else -> anyOrNull(column.columnAlias)
        }
    }
    return jsonMapper.convertValue(fromValue, jacksonTypeRef())
}
