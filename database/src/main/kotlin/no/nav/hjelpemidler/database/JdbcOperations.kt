package no.nav.hjelpemidler.database

import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass

/**
 * Interface for kommunikasjon med en database basert på JDBC.
 */
interface JdbcOperations : DatabaseOperations {
    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T

    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        type: KClass<T>,
    ): T = single(sql, queryParameters) { it.toValue(type) }

    fun <T : Any> singleOrNull(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): T?

    fun <T : Any> singleOrNull(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        type: KClass<T>,
    ): T? = singleOrNull(sql, queryParameters) { it.toValueOrNull(type) }

    fun <T : Any> list(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        mapper: ResultMapper<T>,
    ): List<T>

    fun <T : Any> list(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        type: KClass<T>,
    ): List<T> = list(sql, queryParameters) { it.toValue(type) }

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
    ): List<Int> = batch(sql, items.map(block))

    fun batchAndReturnGeneratedKeys(
        @Language("SQL") sql: CharSequence,
        queryParameters: Collection<QueryParameters> = emptyList(),
    ): List<Long>

    fun <T : Any> batchAndReturnGeneratedKeys(
        @Language("SQL") sql: CharSequence,
        items: Collection<T> = emptyList(),
        block: (T) -> QueryParameters,
    ): List<Long> = batchAndReturnGeneratedKeys(sql, items.map(block))
}
