package no.nav.hjelpemidler.database.repository

import org.hibernate.LockMode
import org.hibernate.graph.RootGraph
import org.hibernate.query.NativeQuery
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
 * @see [RepositoryOperations.createNativeQuery]
 */
inline fun <reified T : Any> RepositoryOperations.createNativeQuery(@Language("SQL") sql: CharSequence): NativeQuery<T> =
    createNativeQuery(sql, T::class)

/**
 * @see [RepositoryOperations.createRepository]
 */
inline fun <reified T : Any, ID : Any> RepositoryOperations.createRepository(): Repository<T, ID> =
    createRepository(T::class)
