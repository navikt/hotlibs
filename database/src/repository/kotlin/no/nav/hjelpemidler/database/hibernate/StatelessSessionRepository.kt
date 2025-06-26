package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.ReadOperations
import no.nav.hjelpemidler.database.repository.Repository
import no.nav.hjelpemidler.database.repository.WriteOperations
import org.hibernate.StatelessSession
import kotlin.reflect.KClass

/**
 * Implementasjon av [Repository] basert på [org.hibernate.StatelessSession].
 */
internal class StatelessSessionRepository<T : Any, ID : Any> private constructor(
    private val readOperations: ReadOperations<T, ID>,
    private val writeOperations: WriteOperations<T, ID>,
) : Repository<T, ID>, ReadOperations<T, ID> by readOperations, WriteOperations<T, ID> by writeOperations {
    constructor(entityClass: KClass<T>, session: StatelessSession) : this(
        readOperations = StatelessSessionReadOperations(entityClass, session),
        writeOperations = StatelessSessionWriteOperations(session)
    )
}
