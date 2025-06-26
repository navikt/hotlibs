package no.nav.hjelpemidler.database.repository

interface WriteOperations<T : Any, ID : Any> {
    /**
     * @see [org.hibernate.StatelessSession.insert]
     */
    fun insert(entity: T): ID

    /**
     * @see [org.hibernate.StatelessSession.insertMultiple]
     */
    fun insertAll(entities: Iterable<T>)

    /**
     * @see [org.hibernate.StatelessSession.update]
     */
    fun update(entity: T)

    /**
     * @see [org.hibernate.StatelessSession.updateMultiple]
     */
    fun updateAll(entities: Iterable<T>)

    /**
     * @see [org.hibernate.StatelessSession.delete]
     */
    fun delete(entity: T)

    /**
     * @see [org.hibernate.StatelessSession.deleteMultiple]
     */
    fun deleteAll(entities: Iterable<T>)

    /**
     * @see [org.hibernate.StatelessSession.upsert]
     */
    fun upsert(entity: T)

    /**
     * @see [org.hibernate.StatelessSession.upsertMultiple]
     */
    fun upsertAll(entities: Iterable<T>)
}
