package no.nav.hjelpemidler.database

import org.intellij.lang.annotations.Language

typealias Session = kotliquery.Session
typealias ResultMapper<T> = (Row) -> T?

fun <T : Any> Session.single(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T =
    query(sql.toString(), queryParameters, mapper) ?: throw NoSuchElementException("Forventet en verdi, men var null")

fun <T> Session.query(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): T? =
    single(queryOf(sql.toString(), queryParameters), mapper)

fun <T : Any> Session.queryList(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
    mapper: ResultMapper<T>,
): List<T> =
    list(queryOf(sql.toString(), queryParameters), mapper)

fun <T : Any> Session.queryPage(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
    limit: Int,
    offset: Int,
    totalNumberOfItemsLabel: String = "total",
    mapper: ResultMapper<T>,
): Page<T> {
    val limitParameter = "no_nav_hjelpemidler_database_limit"
    val offsetParameter = "no_nav_hjelpemidler_database_offset"
    var totalNumberOfItems = -1
    val items = list(
        queryOf(
            statement = """
                $sql
                LIMIT :$limitParameter
                OFFSET :$offsetParameter
            """.trimIndent(),
            queryParameters + mapOf(
                limitParameter to limit + 1, // hent limit + 1 for å sjekke "hasMore"
                offsetParameter to offset,
            )
        )
    ) { row ->
        totalNumberOfItems = row.intOrNull(totalNumberOfItemsLabel) ?: -1
        mapper(row)
    }
    return Page(
        items = items.take(limit),
        total = totalNumberOfItems,
    )
}

fun Session.execute(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): Boolean = execute(queryOf(sql.toString(), queryParameters))

fun Session.update(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): UpdateResult = UpdateResult(update(queryOf(sql.toString(), queryParameters)))

fun Session.updateAndReturnGeneratedKey(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): Long = checkNotNull(updateAndReturnGeneratedKey(queryOf(sql.toString(), queryParameters))) {
    "Forventet en generert nøkkel, men var null"
}

fun Session.batch(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Int> = batchPreparedNamedStatement(sql.toString(), queryParameters.prepare())

fun <T : Any> Session.batch(
    @Language("PostgreSQL") sql: CharSequence,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Int> = batch(sql, items.map(block))

fun Session.batchAndReturnGeneratedKeys(
    @Language("PostgreSQL") sql: CharSequence,
    queryParameters: Collection<QueryParameters> = emptyList(),
): List<Long> = batchPreparedNamedStatementAndReturnGeneratedKeys(sql.toString(), queryParameters.prepare())

fun <T : Any> Session.batchAndReturnGeneratedKeys(
    @Language("PostgreSQL") sql: CharSequence,
    items: Collection<T> = emptyList(),
    block: (T) -> QueryParameters,
): List<Long> = batchAndReturnGeneratedKeys(sql, items.map(block))
