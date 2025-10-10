package no.nav.hjelpemidler.database.jdbc

import kotliquery.Session
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.Page
import no.nav.hjelpemidler.database.PageRequest
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.ResultMapper
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.UpdateResult
import no.nav.hjelpemidler.database.pageOf
import no.nav.hjelpemidler.database.prepare
import no.nav.hjelpemidler.database.queryOf
import java.io.Closeable

/**
 * Implementasjon av [no.nav.hjelpemidler.database.JdbcOperations] basert på [kotliquery.Session].
 */
internal class SessionJdbcOperations(private val session: Session) : JdbcOperations, Closeable by session {
    override fun <T : Any> single(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T = singleOrNull(sql, queryParameters, mapper)
        ?: throw NoSuchElementException("Spørringen ga ingen treff i databasen")

    override fun <T : Any> singleOrNull(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T? = session.single(queryOf(sql, queryParameters)) { mapper(Row(it)) }

    override fun <T : Any> list(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): List<T> = session.list(queryOf(sql, queryParameters)) { mapper(Row(it)) }

    /**
     * NB! Implementasjonen fungerer ikke med Oracle pt.
     */
    override fun <T : Any> page(
        sql: CharSequence,
        queryParameters: QueryParameters,
        pageRequest: PageRequest,
        totalElementsLabel: String,
        mapper: ResultMapper<T>,
    ): Page<T> {
        val limit = pageRequest.limit
        val offset = pageRequest.offset
        var totalElements: Long = -1

        val query = if (pageRequest === PageRequest.Companion.ALL) {
            queryOf(sql, queryParameters)
        } else {
            val limitParameter = "no_nav_hjelpemidler_database_limit"
            val offsetParameter = "no_nav_hjelpemidler_database_offset"

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
        }

        val content = session.list(query) {
            totalElements = it.longOrNull(totalElementsLabel) ?: -1
            mapper(Row(it))
        }

        return pageOf(
            content = content.take(limit),
            totalElements = totalElements,
            pageRequest = pageRequest,
        )
    }

    override fun execute(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): Boolean = session.execute(queryOf(sql, queryParameters))

    override fun update(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): UpdateResult = UpdateResult(session.update(queryOf(sql, queryParameters)))

    override fun updateAndReturnGeneratedKey(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): Long = checkNotNull(session.updateAndReturnGeneratedKey(queryOf(sql, queryParameters))) {
        "Generert nøkkel mangler"
    }

    override fun batch(
        sql: CharSequence,
        queryParameters: Collection<QueryParameters>,
    ): List<Int> = session.batchPreparedNamedStatement(sql.toString(), queryParameters.prepare())

    override fun batchAndReturnGeneratedKeys(
        sql: CharSequence,
        queryParameters: Collection<QueryParameters>,
    ): List<Long> = session.batchPreparedNamedStatementAndReturnGeneratedKeys(sql.toString(), queryParameters.prepare())
}
