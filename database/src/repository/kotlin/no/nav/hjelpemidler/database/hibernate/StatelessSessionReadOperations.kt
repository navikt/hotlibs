package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.ReadOperations
import org.hibernate.LockMode
import org.hibernate.StatelessSession
import kotlin.reflect.KClass

/**
 * Implementasjon av [ReadOperations] basert på [org.hibernate.StatelessSession].
 */
internal class StatelessSessionReadOperations<T : Any, ID : Any>(
    private val entityClass: KClass<T>,
    private val session: StatelessSession,
) : ReadOperations<T, ID>, AutoCloseable by session {
    override fun findById(id: ID, lockMode: LockMode): T =
        session.get<T>(entityClass.java, id, lockMode)

    override fun findAllById(ids: Iterable<ID>, lockMode: LockMode): List<T> =
        session.getMultiple<T>(entityClass.java, ids.toList(), lockMode)

    override fun fetch(association: Any) =
        session.fetch(association)

    override fun refresh(entity: T, lockMode: LockMode) =
        session.refresh(entity, lockMode)
}
