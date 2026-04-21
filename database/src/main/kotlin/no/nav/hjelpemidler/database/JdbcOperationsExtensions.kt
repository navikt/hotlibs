package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.collections.toEnumSet
import org.intellij.lang.annotations.Language

inline fun <reified T : Any> JdbcOperations.single(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T = single(sql, queryParameters) { it.valueOrNull<T>(1) }

inline fun <reified T : Any> JdbcOperations.singleOrNull(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T? = singleOrNull(sql, queryParameters) { it.valueOrNull<T>(1) }

inline fun <reified T : Any> JdbcOperations.list(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): List<T> = list(sql, queryParameters) { it.valueOrNull<T>(1) }

inline fun <reified E : Enum<E>> JdbcOperations.enum(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): E = single(sql, queryParameters) { it.enumOrNull<E>(1) }

inline fun <reified E : Enum<E>> JdbcOperations.enumOrNull(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): E? = singleOrNull(sql, queryParameters) { it.enumOrNull<E>(1) }

inline fun <reified E : Enum<E>> JdbcOperations.enums(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): Set<E> = list(sql, queryParameters) { it.enumOrNull<E>(1) }.toEnumSet()
