package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.sql.Sql
import org.intellij.lang.annotations.Language

/**
 * Interface for kommunikasjon med en PostgreSQL-database.
 */
interface JdbcOperations {
    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        sql: Sql,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T

    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T = single(Sql(sql), queryParameters, mapper)

    fun <T> singleOrNull(
        sql: Sql,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T?

    fun <T> singleOrNull(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T? = singleOrNull(Sql(sql), queryParameters, mapper)

    fun <T : Any> list(
        sql: Sql,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): List<T>

    fun <T : Any> list(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): List<T> = list(Sql(sql), queryParameters, mapper)

    fun <T : Any> page(
        sql: Sql,
        queryParameters: QueryParameters = emptyMap(),
        pageRequest: PageRequest,
        totalNumberOfItemsLabel: String = "total",
        mapper: ResultMapper<T>,
    ): Page<T>

    fun <T : Any> page(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
        pageRequest: PageRequest,
        totalNumberOfItemsLabel: String = "total",
        mapper: ResultMapper<T>,
    ): Page<T> = page(Sql(sql), queryParameters, pageRequest, totalNumberOfItemsLabel, mapper)

    fun execute(sql: Sql, queryParameters: QueryParameters = emptyMap()): Boolean
    fun execute(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
    ): Boolean = execute(Sql(sql), queryParameters)

    fun update(sql: Sql, queryParameters: QueryParameters = emptyMap()): UpdateResult
    fun update(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
    ): UpdateResult = update(Sql(sql), queryParameters)

    fun updateAndReturnGeneratedKey(sql: Sql, queryParameters: QueryParameters = emptyMap()): Long
    fun updateAndReturnGeneratedKey(
        @Language("SQL") sql: String,
        queryParameters: QueryParameters = emptyMap(),
    ): Long = updateAndReturnGeneratedKey(Sql(sql), queryParameters)

    fun batch(sql: Sql, queryParameters: Collection<QueryParameters> = emptyList()): List<Int>
    fun batch(
        @Language("SQL") sql: String,
        queryParameters: Collection<QueryParameters> = emptyList(),
    ): List<Int> = batch(Sql(sql), queryParameters)

    fun <T : Any> batch(sql: Sql, items: Collection<T> = emptyList(), block: (T) -> QueryParameters): List<Int>
    fun <T : Any> batch(
        @Language("SQL") sql: String,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Int> = batch(Sql(sql), items, block)

    fun batchAndReturnGeneratedKeys(sql: Sql, queryParameters: Collection<QueryParameters> = emptyList()): List<Long>
    fun batchAndReturnGeneratedKeys(
        @Language("SQL") sql: String,
        queryParameters: Collection<QueryParameters> = emptyList(),
    ): List<Long> = batchAndReturnGeneratedKeys(Sql(sql), queryParameters.prepare())

    fun <T : Any> batchAndReturnGeneratedKeys(
        sql: Sql,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Long>

    fun <T : Any> batchAndReturnGeneratedKeys(
        @Language("SQL") sql: String,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Long> = batchAndReturnGeneratedKeys(Sql(sql), items, block)
}
