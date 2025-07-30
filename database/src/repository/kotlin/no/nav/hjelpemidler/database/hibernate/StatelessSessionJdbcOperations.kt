package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.Page
import no.nav.hjelpemidler.database.PageRequest
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.ResultMapper
import no.nav.hjelpemidler.database.UpdateResult
import org.hibernate.StatelessSession

class StatelessSessionJdbcOperations(
    private val session: StatelessSession,
) : JdbcOperations, AutoCloseable by session {
    override fun <T : Any> single(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T = doReturningWork { it.single(sql, queryParameters, mapper) }

    override fun <T> singleOrNull(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): T? = doReturningWork { it.singleOrNull(sql, queryParameters, mapper) }

    override fun <T : Any> list(
        sql: CharSequence,
        queryParameters: QueryParameters,
        mapper: ResultMapper<T>,
    ): List<T> = doReturningWork { it.list(sql, queryParameters, mapper) }

    override fun <T : Any> page(
        sql: CharSequence,
        queryParameters: QueryParameters,
        pageRequest: PageRequest,
        totalElementsLabel: String,
        mapper: ResultMapper<T>,
    ): Page<T> = doReturningWork { it.page(sql, queryParameters, pageRequest, totalElementsLabel, mapper) }

    override fun execute(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): Boolean = doReturningWork { it.execute(sql, queryParameters) }

    override fun update(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): UpdateResult = doReturningWork { it.update(sql, queryParameters) }

    override fun updateAndReturnGeneratedKey(
        sql: CharSequence,
        queryParameters: QueryParameters,
    ): Long = doReturningWork { it.updateAndReturnGeneratedKey(sql, queryParameters) }

    override fun batch(
        sql: CharSequence,
        queryParameters: Collection<QueryParameters>,
    ): List<Int> = doReturningWork { it.batch(sql, queryParameters) }

    override fun batchAndReturnGeneratedKeys(
        sql: CharSequence,
        queryParameters: Collection<QueryParameters>,
    ): List<Long> = doReturningWork { it.batchAndReturnGeneratedKeys(sql, queryParameters) }

    private fun <T> doReturningWork(block: (JdbcOperations) -> T): T = session.doReturningWork { connection ->
        block(JdbcOperations(connection))
    }
}
