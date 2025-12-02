package no.nav.hjelpemidler.database

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.intellij.lang.annotations.Language

inline fun <reified T : Any> JdbcOperations.single(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T = single(sql, queryParameters, jacksonTypeRef<T>())

inline fun <reified T> JdbcOperations.singleOrNull(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T? = singleOrNull(sql, queryParameters, jacksonTypeRef<T>())

inline fun <reified T : Any> JdbcOperations.list(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): List<T> = list(sql, queryParameters, jacksonTypeRef<T>())
