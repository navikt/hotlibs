package no.nav.hjelpemidler.database

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

data class SchemaTable<T : Any>(
    val table: Table,
    val constructor: KFunction<T>,
    val columns: List<SchemaColumn>,
) {
    val tableName = table.name
    val columnAliases = columns.joinToString(", ") { column ->
        "${column.columnName} AS ${column.columnAlias}"
    }
}

data class SchemaColumn(
    val table: Table,
    val parameter: KParameter,
    val column: Column,
) {
    val parameterName = checkNotNull(parameter.name) {
        "$parameter mangler navn"
    }
    val columnName = column.name
    val columnAlias = "${table.name}__${column.name}"
}

fun <T : Any> KClass<T>.schema(): SchemaTable<T> {
    check(isData) {
        "$this er ikke en data-klasse"
    }
    val table = checkNotNull(findAnnotation<Table>()) {
        "$this mangler Table-annotasjon"
    }
    val constructor = checkNotNull(primaryConstructor) {
        "$this mangler konstruktør"
    }
    return SchemaTable(
        table = table,
        constructor = constructor,
        columns = constructor
            .valueParameters
            .map { parameter ->
                SchemaColumn(
                    table = table,
                    parameter = parameter,
                    column = when (val column = parameter.findAnnotation<Column>()) {
                        null -> Column(checkNotNull(parameter.name) {
                            "$this mangler navn"
                        })

                        else -> column
                    },
                )
            },
    )
}
