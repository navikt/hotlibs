package no.nav.hjelpemidler.database.hibernate

import jakarta.persistence.EntityGraph
import no.nav.hjelpemidler.database.QueryParameters
import no.nav.hjelpemidler.database.UpdateResult
import no.nav.hjelpemidler.database.repository.Repository
import no.nav.hjelpemidler.database.repository.RepositoryOperations
import no.nav.hjelpemidler.database.repository.WriteOperations
import org.hibernate.LockMode
import org.hibernate.StatelessSession
import org.hibernate.graph.GraphSemantic
import org.hibernate.graph.RootGraph
import org.hibernate.query.MutationQuery
import org.hibernate.query.NativeQuery
import org.hibernate.query.Query
import kotlin.reflect.KClass

/**
 * Implementasjon av [RepositoryOperations] basert på [org.hibernate.StatelessSession].
 */
internal class StatelessSessionRepositoryOperations private constructor(
    private val session: StatelessSession,
    private val writeOperations: WriteOperations<Any, Any>,
) : RepositoryOperations, WriteOperations<Any, Any> by writeOperations, AutoCloseable by session {
    constructor(session: StatelessSession) : this(
        session = session,
        writeOperations = StatelessSessionWriteOperations(session),
    )

    override fun fetch(association: Any) =
        session.fetch(association)

    override fun refresh(entity: Any, lockMode: LockMode) =
        session.refresh(entity, lockMode)

    override fun <T : Any> findById(
        entityClass: KClass<T>,
        id: Any,
        lockMode: LockMode,
    ): T =
        session.get(entityClass.java, id, lockMode)

    override fun <T : Any> findById(
        entityGraph: EntityGraph<T>,
        id: Any,
        lockMode: LockMode,
        graphSemantic: GraphSemantic,
    ): T =
        session.get(entityGraph, graphSemantic, id, lockMode)

    override fun <T : Any> findAllById(
        entityClass: KClass<T>,
        ids: Iterable<*>,
        lockMode: LockMode,
    ): List<T> =
        session.getMultiple(entityClass.java, ids.toList(), lockMode)

    override fun <T : Any> findAllById(
        entityGraph: EntityGraph<T>,
        ids: Iterable<*>,
        graphSemantic: GraphSemantic,
    ): List<T> =
        session.getMultiple(entityGraph, graphSemantic, ids.toList())

    override fun <T : Any> createEntityGraph(rootType: KClass<T>): RootGraph<T> =
        session.createEntityGraph(rootType.java)

    override fun <T : Any> createQuery(
        hql: CharSequence,
        resultClass: KClass<T>,
    ): Query<T> =
        session.createQuery(hql.toString(), resultClass.java)

    override fun createMutationQuery(
        hql: CharSequence,
    ): MutationQuery =
        session.createMutationQuery(hql.toString())

    override fun <T : Any> createNativeQuery(
        sql: CharSequence,
        resultClass: KClass<T>,
    ): NativeQuery<T> =
        session.createNativeQuery(sql.toString(), resultClass.java)

    override fun createNativeMutationQuery(
        sql: CharSequence,
    ): MutationQuery =
        session.createNativeMutationQuery(sql.toString())

    override fun <T : Any, ID : Any> createRepository(entityClass: KClass<T>): Repository<T, ID> =
        StatelessSessionRepository(entityClass, session)

    override fun <T : Any> single(
        sql: CharSequence,
        queryParameters: QueryParameters,
        resultClass: KClass<T>,
    ): T {
        val query = createNativeQuery(sql, resultClass)
        queryParameters.forEach(query::setParameter)
        return query.singleResult
    }

    override fun <T : Any> singleOrNull(
        sql: CharSequence,
        queryParameters: QueryParameters,
        resultClass: KClass<T>,
    ): T? {
        val query = createNativeQuery(sql, resultClass)
        queryParameters.forEach(query::setParameter)
        return query.singleResultOrNull
    }

    override fun <T : Any> list(
        sql: CharSequence,
        queryParameters: QueryParameters,
        resultClass: KClass<T>,
    ): List<T> {
        val query = createNativeQuery(sql, resultClass)
        queryParameters.forEach(query::setParameter)
        return query.resultList
    }

    override fun update(sql: CharSequence, queryParameters: QueryParameters): UpdateResult {
        val query = createNativeMutationQuery(sql)
        queryParameters.forEach(query::setParameter)
        return UpdateResult(query.executeUpdate())
    }
}
