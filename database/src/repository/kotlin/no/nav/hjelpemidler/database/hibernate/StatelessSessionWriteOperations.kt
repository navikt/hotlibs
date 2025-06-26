package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.WriteOperations
import org.hibernate.StatelessSession

/**
 * Implementasjon av [WriteOperations] basert på [org.hibernate.StatelessSession].
 */
internal class StatelessSessionWriteOperations<T : Any, ID : Any>(
    private val session: StatelessSession,
) : WriteOperations<T, ID>, AutoCloseable by session {
    @Suppress("UNCHECKED_CAST")
    override fun insert(entity: T): ID =
        session.insert(entity) as ID

    override fun insertAll(entities: Iterable<T>) =
        session.insertMultiple(entities.toList())

    override fun update(entity: T) =
        session.update(entity)

    override fun updateAll(entities: Iterable<T>) =
        session.updateMultiple(entities.toList())

    override fun delete(entity: T) =
        session.delete(entity)

    override fun deleteAll(entities: Iterable<T>) =
        session.deleteMultiple(entities.toList())

    override fun upsert(entity: T) =
        session.upsert(entity)

    override fun upsertAll(entities: Iterable<T>) =
        session.upsertMultiple(entities.toList())
}
