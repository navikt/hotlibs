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
    single(queryOf(sql, queryParameters), mapper)

fun <T> Session.single(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T =
    checkNotNull(query(sql, queryParameters, mapper)) {
        "Forventet en verdi, men var null"
    }

fun <T> Session.queryList(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> =
    list(queryOf(sql, queryParameters), mapper)

fun <T> Session.queryPage(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    run(queryOf(sql, queryParameters).map(mapper).asPage(limit, offset, totalNumberOfItemsLabel))

fun Session.execute(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): Boolean =
    execute(queryOf(sql, queryParameters))

fun Session.update(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult =
    UpdateResult(actualRowCount = update(queryOf(sql, queryParameters)))

fun Session.updateAndReturnGeneratedKey(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): Long =
    checkNotNull(updateAndReturnGeneratedKey(queryOf(sql, queryParameters))) {
        "Forventet en generert nøkkel, men var null"
    }

fun Session.batch(
    @Language("PostgreSQL") sql: String,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Int> =
    batchPreparedNamedStatement(sql, queryParameters)

fun Session.batchAndReturnGeneratedKeys(
    @Language("PostgreSQL") sql: String,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Long> =
    batchPreparedNamedStatementAndReturnGeneratedKeys(sql, queryParameters)

fun <T> Session.batch(
    @Language("PostgreSQL") sql: String,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Int> =
    batch(sql, items.map(block))

fun <T> Collection<T>.batch(
    session: Session,
    @Language("PostgreSQL") sql: String,
    block: (T) -> QueryParameters,
): List<Int> =
    session.batch(sql, map(block))

internal fun <A> ResultQueryActionBuilder<A>.asPage(
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
): PageResultQueryAction<A> =
    PageResultQueryAction(
        query = query,
        extractor = extractor,
        limit = limit,
        offset = offset,
        totalNumberOfItemsLabel = totalNumberOfItemsLabel,
    )

internal fun <A> Session.run(action: PageResultQueryAction<A>): Page<A> =
    action.runWithSession(this)
