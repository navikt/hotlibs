package no.nav.hjelpemidler.database

import org.intellij.lang.annotations.Language

inline fun <reified T : Any> JdbcOperations.single(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T = single(sql, queryParameters) { it.toValue<T>() }

inline fun <reified T : Any> JdbcOperations.singleOrNull(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T? = singleOrNull(sql, queryParameters) { it.toValue<T>() }

inline fun <reified T : Any> JdbcOperations.list(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): List<T> = list(sql, queryParameters) { it.toValue<T>() }
