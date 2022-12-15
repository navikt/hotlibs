package no.nav.hjelpemidler.database

import kotliquery.Row
import kotliquery.Session
import kotliquery.action.ResultQueryActionBuilder
import kotliquery.queryOf
import org.intellij.lang.annotations.Language

typealias QueryParameters = Map<String, Any?>
typealias ResultMapper<T> = (Row) -> T?

fun <T> Session.query(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? =
    run(queryOf(sql, queryParameters).map(mapper).asSingle)

fun <T> Session.queryList(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> =
    run(queryOf(sql, queryParameters).map(mapper).asList)

fun <T> Session.queryPage(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    run(queryOf(sql, queryParameters, totalNumberOfItemsLabel).map(mapper).asPage(limit, offset))

fun Session.execute(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): Boolean =
    run(queryOf(sql, queryParameters).asExecute)

fun Session.update(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult =
    UpdateResult(rowCount = run(queryOf(sql, queryParameters).asUpdate), generatedId = null)

fun Session.updateAndReturnGeneratedKey(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult =
    UpdateResult(rowCount = null, generatedId = run(queryOf(sql, queryParameters).asUpdateAndReturnGeneratedKey))

internal fun <A> ResultQueryActionBuilder<A>.asPage(limit: Int, offset: Int): PageResultQueryAction<A> =
    PageResultQueryAction(query, extractor, limit, offset)

internal fun <A> Session.run(action: PageResultQueryAction<A>): Page<A> =
    action.runWithSession(this)
