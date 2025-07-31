package no.nav.hjelpemidler.database.repository

import no.nav.hjelpemidler.database.QueryParameters
import org.hibernate.LockMode
import org.hibernate.graph.RootGraph
import org.hibernate.query.NativeQuery
import org.hibernate.query.Query
import org.intellij.lang.annotations.Language

/**
 * @see [RepositoryOperations.findById]
 */
inline fun <reified T : Any> RepositoryOperations.findById(
    id: Any,
    lockMode: LockMode = LockMode.NONE,
): T = findById(T::class, id, lockMode)

/**
 * @see [RepositoryOperations.findAllById]
 */
inline fun <reified T : Any> RepositoryOperations.findAllById(
    ids: List<*>,
    lockMode: LockMode = LockMode.NONE,
): List<T> = findAllById(T::class, ids, lockMode)

/**
 * @see [RepositoryOperations.createEntityGraph]
 */
inline fun <reified T : Any> RepositoryOperations.createEntityGraph(): RootGraph<T> =
    createEntityGraph(T::class)

/**
 * @see [RepositoryOperations.createQuery]
 */
inline fun <reified T : Any> RepositoryOperations.createQuery(@Language("HQL") hql: CharSequence): Query<T> =
    createQuery(hql, T::class)

/**
 * @see [RepositoryOperations.createNativeQuery]
 */
inline fun <reified T : Any> RepositoryOperations.createNativeQuery(@Language("SQL") sql: CharSequence): NativeQuery<T> =
    createNativeQuery(sql, T::class)

/**
 * @see [RepositoryOperations.createRepository]
 */
inline fun <reified T : Any, ID : Any> RepositoryOperations.createRepository(): Repository<T, ID> =
    createRepository(T::class)

/**
 * @see [RepositoryOperations.single]
 */
inline fun <reified T : Any> RepositoryOperations.single(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T = single(sql, queryParameters, T::class)

/**
 * @see [RepositoryOperations.single]
 */
inline fun <reified T : Any> RepositoryOperations.singleOrNull(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): T? = singleOrNull(sql, queryParameters, T::class)

/**
 * @see [RepositoryOperations.single]
 */
inline fun <reified T : Any> RepositoryOperations.list(
    @Language("SQL") sql: CharSequence,
    queryParameters: QueryParameters = emptyMap(),
): List<T> = list(sql, queryParameters, T::class)
