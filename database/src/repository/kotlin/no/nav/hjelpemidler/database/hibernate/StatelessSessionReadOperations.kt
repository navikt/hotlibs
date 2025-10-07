package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.ReadOperations
import no.nav.hjelpemidler.database.repository.unwrap
import org.hibernate.LockMode
import org.hibernate.StatelessSession
import kotlin.reflect.KClass

/**
 * Implementasjon av [ReadOperations] basert på [org.hibernate.StatelessSession].
 */
internal class StatelessSessionReadOperations<T : Any, ID : Any>(
    private val session: StatelessSession,
    private val entityClass: KClass<T>,
) : ReadOperations<T, ID>, AutoCloseable by session {
    override fun findById(id: ID, lockMode: LockMode): T? =
        session.get<T>(entityClass.java, id.unwrap(), lockMode)

    override fun findAllById(ids: Iterable<ID>, lockMode: LockMode): List<T?> =
        session.getMultiple<T>(entityClass.java, ids.map { it.unwrap() }, lockMode)

    override fun fetch(association: Any) = session.fetch(association)

    override fun refresh(entity: T, lockMode: LockMode) = session.refresh(entity, lockMode)
}
