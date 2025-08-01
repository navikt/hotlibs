package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.domain.id.Id

interface QueryParameter<out T> {
    val queryParameter: T
}

typealias QueryParameters = Map<String, Any?>

interface QueryParametersProvider {
    fun toQueryParameters(): QueryParameters
}

fun Boolean?.toQueryParameters(key: String): QueryParameters = mapOf(key to this)
fun Id<*>?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this?.value)
fun Int?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)
fun Long?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)
fun String?.toQueryParameters(key: String): QueryParameters = mapOf(key to this)

fun <E : Enum<E>> Enum<E>?.toQueryParameters(key: String): QueryParameters = mapOf(key to this?.name)
fun <T> QueryParameter<T>?.toQueryParameters(key: String): QueryParameters = mapOf(key to this?.queryParameter)

inline fun <reified T : Comparable<T>> Collection<Id<T>>?.toQueryParameters(key: String = "id"): QueryParameters =
    mapOf(key to this?.map(Id<T>::value)?.toTypedArray())

/**
 * Transformer til verdier som støttes av JDBC direkte.
 */
internal fun QueryParameters.prepare(): Map<String, Any?> = mapValues { (_, value) ->
    when (value) {
        is CharSequence -> value.toString()
        is Enum<*> -> value.name
        is Id<*> -> value.value
        is QueryParameter<*> -> value.queryParameter
        else -> value
    }
}

internal fun Collection<QueryParameters>.prepare(): List<Map<String, Any?>> = map(QueryParameters::prepare)

internal fun queryOf(sql: CharSequence, queryParameters: QueryParameters): kotliquery.Query =
    kotliquery.Query(sql.toString(), listOf(), queryParameters.prepare())

/**
 * Legg til [prefix] og/eller [postfix] på [columnLabel].
 */
fun columnLabelOf(columnLabel: String, prefix: String? = null, postfix: String? = null): String =
    listOfNotNull(prefix, columnLabel, postfix).joinToString("_")

/**
 * Legg til [prefix] og/eller [postfix] på alle kolonnenavn i [this].
 */
fun QueryParameters.transform(prefix: String? = null, postfix: String? = null): QueryParameters =
    mapKeys { columnLabelOf(it.key, prefix, postfix) }
