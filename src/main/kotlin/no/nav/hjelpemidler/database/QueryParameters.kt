package no.nav.hjelpemidler.database

import kotliquery.Query
import no.nav.hjelpemidler.database.sql.Sql

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

fun queryOf(sql: Sql, queryParameters: QueryParameters): Query =
    Query(sql.toString(), listOf(), queryParameters.prepare())

fun queryOf(sql: String, queryParameters: QueryParameters): Query =
    queryOf(Sql(sql), queryParameters)
