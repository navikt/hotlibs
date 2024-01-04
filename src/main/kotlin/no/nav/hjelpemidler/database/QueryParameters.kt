package no.nav.hjelpemidler.database

import kotliquery.Query

interface QueryParameter<out T> {
    val value: T
}

typealias QueryParameters = Map<String, Any?>

fun Int?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)
fun Long?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)
fun String?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)
fun <T> QueryParameter<T>?.toQueryParameters(key: String = "id"): QueryParameters = mapOf(key to this)

internal fun QueryParameters.prepare(): Map<String, Any?> = mapValues { (_, value) ->
    when (value) {
        is QueryParameter<*> -> value.value
        is Enum<*> -> value.name
        else -> value
    }
}

internal fun Collection<QueryParameters>.prepare(): List<Map<String, Any?>> = map(QueryParameters::prepare)

fun queryOf(statement: String, queryParameters: QueryParameters): Query =
    Query(statement, listOf(), queryParameters.prepare())
