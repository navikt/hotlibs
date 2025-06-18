package no.nav.hjelpemidler.database

import org.intellij.lang.annotations.Language

/**
 * Interface for kommunikasjon med en database.
 */
interface JdbcOperations {
    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T

    fun <T> singleOrNull(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T?

    fun <T : Any> list(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): List<T>

    fun <T : Any> page(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        pageRequest: PageRequest,
        totalElementsLabel: String = "total_elements",
        mapper: ResultMapper<T>,
    ): Page<T>

    fun execute(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
    ): Boolean

    fun update(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
    ): UpdateResult

    fun updateAndReturnGeneratedKey(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
    ): Long

    fun batch(
        @Language("SQL") sql: CharSequence,
        queryParameters: Collection<QueryParameters> = emptyList(),
    ): List<Int>

    fun <T : Any> batch(
        @Language("SQL") sql: CharSequence,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Int>

    fun batchAndReturnGeneratedKeys(
        @Language("SQL") sql: CharSequence,
        queryParameters: Collection<QueryParameters> = emptyList(),
    ): List<Long>

    fun <T : Any> batchAndReturnGeneratedKeys(
        @Language("SQL") sql: CharSequence,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Long>
}
