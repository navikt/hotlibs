package no.nav.hjelpemidler.database

import kotliquery.Session
import no.nav.hjelpemidler.database.sql.Sql
import java.io.Closeable

internal class SessionJdbcOperations(private val session: Session) : JdbcOperations, Closeable by session {
    override fun <T : Any> single(
        sql: Sql,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T = singleOrNull(sql, queryParameters, mapper)
        ?: throw NoSuchElementException("Spørringen ga ingen treff i databasen")

    override fun <T> singleOrNull(
        sql: Sql,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T? = session.single(queryOf(sql, queryParameters), mapper)

    override fun <T : Any> list(
        sql: Sql,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): List<T> = session.list(queryOf(sql, queryParameters), mapper)

    /**
     * NB! Implementasjonen fungerer ikke med Oracle pt.
     */
    override fun <T : Any> page(
        sql: Sql,
        queryParameters: QueryParameters,
        pageRequest: PageRequest,
        totalElementsLabel: String,
        mapper: ResultMapper<T>,
    ): Page<T> {
        val limitParameter = "no_nav_hjelpemidler_database_limit"
        val offsetParameter = "no_nav_hjelpemidler_database_offset"
        val limit = pageRequest.limit
        val offset = pageRequest.offset
        var totalElements: Long = -1

        val content = session.list(
            queryOf(
                sql = """
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
            totalElements = row.longOrNull(totalElementsLabel) ?: -1
            mapper(row)
        }

        return pageOf(
            content = content.take(limit),
            totalElements = totalElements,
            pageRequest = pageRequest,
        )
    }

    override fun execute(
        sql: Sql,
        queryParameters: QueryParameters,
    ): Boolean = session.execute(queryOf(sql, queryParameters))

    override fun update(
        sql: Sql,
        queryParameters: QueryParameters,
    ): UpdateResult = UpdateResult(session.update(queryOf(sql, queryParameters)))

    override fun updateAndReturnGeneratedKey(
        sql: Sql,
        queryParameters: QueryParameters,
    ): Long = checkNotNull(session.updateAndReturnGeneratedKey(queryOf(sql, queryParameters))) {
        "Generert nøkkel mangler"
    }

    override fun batch(
        sql: Sql,
        queryParameters: Collection<QueryParameters>,
    ): List<Int> = session.batchPreparedNamedStatement(sql.toString(), queryParameters.prepare())

    override fun <T : Any> batch(
        sql: Sql,
        items: Collection<T>,
        block: (T) -> QueryParameters,
    ): List<Int> = batch(sql, items.map(block))

    override fun batchAndReturnGeneratedKeys(
        sql: Sql,
        queryParameters: Collection<QueryParameters>,
    ): List<Long> = session.batchPreparedNamedStatementAndReturnGeneratedKeys(sql.toString(), queryParameters.prepare())

    override fun <T : Any> batchAndReturnGeneratedKeys(
        sql: Sql,
        items: Collection<T>,
        block: (T) -> QueryParameters,
    ): List<Long> = batchAndReturnGeneratedKeys(sql, items.map(block))
}
