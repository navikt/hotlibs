package no.nav.hjelpemidler.database.repository

import jakarta.persistence.EntityGraph
import no.nav.hjelpemidler.database.DatabaseOperations
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.UpdateResult
import org.hibernate.LockMode
import org.hibernate.graph.GraphSemantic
import org.hibernate.graph.RootGraph
import org.hibernate.query.MutationQuery
import org.hibernate.query.NativeQuery
import org.hibernate.query.Query
import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass

/**
 * Interface for kommunikasjon med en database basert på JPA/Hibernate.
 */
interface RepositoryOperations : DatabaseOperations, WriteOperations<Any, Any>, JdbcOperations {
    /**
     * @see [org.hibernate.StatelessSession.fetch]
     */
    fun fetch(association: Any)

    /**
     * @see [org.hibernate.StatelessSession.refresh]
     */
    fun refresh(entity: Any, lockMode: LockMode = LockMode.NONE)

    /**
     * @see [org.hibernate.StatelessSession.get]
     */
    fun <T : Any> findById(
        entityClass: KClass<T>,
        id: Any,
        lockMode: LockMode = LockMode.NONE,
    ): T

    /**
     * @see [org.hibernate.StatelessSession.get]
     */
    fun <T : Any> findById(
        entityGraph: EntityGraph<T>,
        id: Any,
        lockMode: LockMode = LockMode.NONE,
        graphSemantic: GraphSemantic = GraphSemantic.LOAD,
    ): T

    /**
     * @see [org.hibernate.StatelessSession.getMultiple]
     */
    fun <T : Any> findAllById(
        entityClass: KClass<T>,
        ids: Iterable<*>,
        lockMode: LockMode = LockMode.NONE,
    ): List<T>

    /**
     * @see [org.hibernate.StatelessSession.getMultiple]
     */
    fun <T : Any> findAllById(
        entityGraph: EntityGraph<T>,
        ids: Iterable<*>,
        graphSemantic: GraphSemantic = GraphSemantic.LOAD,
    ): List<T>

    /**
     * @see [org.hibernate.StatelessSession.createEntityGraph]
     */
    fun <T : Any> createEntityGraph(rootType: KClass<T>): RootGraph<T>

    /**
     * @see [org.hibernate.StatelessSession.createQuery]
     */
    fun <T : Any> createQuery(@Language("HQL") hql: CharSequence, resultClass: KClass<T>): Query<T>

    /**
     * @see [org.hibernate.StatelessSession.createMutationQuery]
     */
    fun createMutationQuery(@Language("HQL") hql: CharSequence): MutationQuery

    /**
     * @see [org.hibernate.StatelessSession.createNativeQuery]
     */
    fun <T : Any> createNativeQuery(@Language("SQL") sql: CharSequence, resultClass: KClass<T>): NativeQuery<T>

    /**
     * @see [org.hibernate.StatelessSession.createNativeMutationQuery]
     */
    fun createNativeMutationQuery(@Language("HQL") sql: CharSequence): MutationQuery

    fun <T : Any, ID : Any> createRepository(entityClass: KClass<T>): Repository<T, ID>

    /**
     * @throws NoSuchElementException hvis spørringen ikke gir treff i databasen
     */
    fun <T : Any> single(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        resultClass: KClass<T>,
    ): T

    fun <T : Any> singleOrNull(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        resultClass: KClass<T>,
    ): T?

    fun <T : Any> list(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters = emptyMap(),
        resultClass: KClass<T>,
    ): List<T>

    override fun update(
        @Language("SQL") sql: CharSequence,
        queryParameters: QueryParameters,
    ): UpdateResult
}
