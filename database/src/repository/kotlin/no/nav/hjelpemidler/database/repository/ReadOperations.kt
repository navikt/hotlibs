package no.nav.hjelpemidler.database.repository

import org.hibernate.LockMode

interface ReadOperations<T : Any, ID : Any> {
    /**
     * @see [org.hibernate.StatelessSession.get]
     */
    fun findById(id: ID, lockMode: LockMode = LockMode.NONE): T?

    /**
     * @see [org.hibernate.StatelessSession.getMultiple]
     */
    fun findAllById(ids: Iterable<ID>, lockMode: LockMode = LockMode.NONE): List<T?>

    /**
     * @see [org.hibernate.StatelessSession.fetch]
     */
    fun fetch(association: Any)

    /**
     * @see [org.hibernate.StatelessSession.refresh]
     */
    fun refresh(entity: T, lockMode: LockMode = LockMode.NONE)
}
