package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.sql.Sql
import org.intellij.lang.annotations.Language

typealias Session = kotliquery.Session
typealias ResultMapper<T> = (Row) -> T?

val Session.jdbcOperations: JdbcOperations get() = SessionJdbcOperations(this)

fun <T : Any> Session.single(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T = jdbcOperations.single(sql, queryParameters, mapper)

fun <T : Any> Session.single(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T = jdbcOperations.single(sql, queryParameters, mapper)

fun <T> Session.singleOrNull(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? = jdbcOperations.singleOrNull(sql, queryParameters, mapper)

fun <T> Session.singleOrNull(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? = jdbcOperations.singleOrNull(sql, queryParameters, mapper)

@Deprecated("Bruk singleOrNull", ReplaceWith("this.singleOrNull(sql, queryParameters, mapper)"))
fun <T> Session.query(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? = jdbcOperations.singleOrNull(sql, queryParameters, mapper)

@Deprecated("Bruk singleOrNull", ReplaceWith("this.singleOrNull(sql, queryParameters, mapper)"))
fun <T> Session.query(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? = jdbcOperations.singleOrNull(sql, queryParameters, mapper)

fun <T : Any> Session.list(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> = jdbcOperations.list(sql, queryParameters, mapper)

fun <T : Any> Session.list(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> = jdbcOperations.list(sql, queryParameters, mapper)

@Deprecated("Bruk list", ReplaceWith("this.list(sql, queryParameters, mapper)"))
fun <T : Any> Session.queryList(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> = jdbcOperations.list(sql, queryParameters, mapper)

@Deprecated("Bruk list", ReplaceWith("this.list(sql, queryParameters, mapper)"))
fun <T : Any> Session.queryList(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> = jdbcOperations.list(sql, queryParameters, mapper)

fun <T : Any> Session.page(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    jdbcOperations.page(sql, queryParameters, pageRequestFromOffset(limit, offset), totalNumberOfItemsLabel, mapper)

fun <T : Any> Session.page(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    jdbcOperations.page(sql, queryParameters, pageRequestFromOffset(limit, offset), totalNumberOfItemsLabel, mapper)

@Deprecated("Bruk page", ReplaceWith("this.page(sql, queryParameters, limit, offset, totalNumberOfItemsLabel, mapper)"))
fun <T : Any> Session.queryPage(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    jdbcOperations.page(sql, queryParameters, pageRequestFromOffset(limit, offset), totalNumberOfItemsLabel, mapper)

@Deprecated("Bruk page", ReplaceWith("this.page(sql, queryParameters, limit, offset, totalNumberOfItemsLabel, mapper)"))
fun <T : Any> Session.queryPage(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> =
    jdbcOperations.page(sql, queryParameters, pageRequestFromOffset(limit, offset), totalNumberOfItemsLabel, mapper)

fun Session.execute(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): Boolean = jdbcOperations.execute(sql, queryParameters)

fun Session.execute(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
): Boolean = jdbcOperations.execute(sql, queryParameters)

fun Session.update(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult = jdbcOperations.update(sql, queryParameters)

fun Session.update(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult = jdbcOperations.update(sql, queryParameters)

fun Session.updateAndReturnGeneratedKey(
    @Language("PostgreSQL") sql: String,
    queryParameters: QueryParameters = emptyMap(),
): Long = jdbcOperations.updateAndReturnGeneratedKey(sql, queryParameters)

fun Session.updateAndReturnGeneratedKey(
    sql: Sql,
    queryParameters: QueryParameters = emptyMap(),
): Long = jdbcOperations.updateAndReturnGeneratedKey(sql, queryParameters)

fun Session.batch(
    @Language("PostgreSQL") sql: String,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Int> = jdbcOperations.batch(sql, queryParameters)

fun Session.batch(
    sql: Sql,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Int> = jdbcOperations.batch(sql, queryParameters)

fun <T : Any> Session.batch(
    @Language("PostgreSQL") sql: String,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Int> = jdbcOperations.batch(sql, items, block)

fun <T : Any> Session.batch(
    sql: Sql,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Int> = jdbcOperations.batch(sql, items, block)

fun Session.batchAndReturnGeneratedKeys(
    @Language("PostgreSQL") sql: String,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Long> = jdbcOperations.batchAndReturnGeneratedKeys(sql, queryParameters)

fun Session.batchAndReturnGeneratedKeys(
    sql: Sql,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Long> = jdbcOperations.batchAndReturnGeneratedKeys(sql, queryParameters)

fun <T : Any> Session.batchAndReturnGeneratedKeys(
    @Language("PostgreSQL") sql: String,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Long> = jdbcOperations.batchAndReturnGeneratedKeys(sql, items, block)

fun <T : Any> Session.batchAndReturnGeneratedKeys(
    sql: Sql,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Long> = jdbcOperations.batchAndReturnGeneratedKeys(sql, items, block)

@Deprecated("Kun for bakoverkompatibilitet", ReplaceWith("PageRequest()"))
internal fun pageRequestFromOffset(limit: Int, offset: Int): PageRequest = PageRequest((offset / limit) + 1, limit)
