package no.nav.hjelpemidler.database.kotliquery

import no.nav.hjelpemidler.database.DatabaseVendorAdapter
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.Page
import no.nav.hjelpemidler.database.PageRequest
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.UpdateResult
import no.nav.hjelpemidler.database.jdbc.vendor
import no.nav.hjelpemidler.database.pageOf
import no.nav.hjelpemidler.database.prepare
import no.nav.hjelpemidler.database.queryOf
import java.io.Closeable

/**
 * Implementasjon av [JdbcOperations] basert på [kotliquery.Session].
 */
internal class SessionJdbcOperations(private val session: kotliquery.Session) : JdbcOperations, Closeable by session {
    private val adapter: DatabaseVendorAdapter = session.connection.underlying.vendor.adapter

    override fun <T : Any> single(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: (Row) -> T?,
    ): T = singleOrNull(sql, queryParameters, mapper) ?: noResultError()

    override fun <T> singleOrNull(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: (Row) -> T?,
    ): T? = session.single(queryOf(sql, queryParameters)) { mapper(adapter.rowFactory(it.underlying)) }

    override fun <T : Any> list(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: (Row) -> T?,
    ): List<T> = session.list(queryOf(sql, queryParameters)) { mapper(adapter.rowFactory(it.underlying)) }

    /**
     * NB! Implementasjonen fungerer ikke med Oracle pt.
     */
    override fun <T : Any> page(
        sql: CharSequence,
        queryParameters: QueryParameters,
        pageRequest: PageRequest,
        totalElementsLabel: String,
        mapper: (Row) -> T?,
    ): Page<T> {
        val limit = pageRequest.limit
        val offset = pageRequest.offset
        var totalElements: Long = -1

        val query = if (pageRequest === PageRequest.ALL) {
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
            mapper(adapter.rowFactory(it.underlying))
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

private fun noResultError(): Nothing = throw NoSuchElementException("Spørringen ga ingen treff i databasen")
