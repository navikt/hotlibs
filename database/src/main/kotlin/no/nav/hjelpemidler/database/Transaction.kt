package no.nav.hjelpemidler.database

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.kotliquery.SessionJdbcOperations
import no.nav.hjelpemidler.database.kotliquery.SessionProperties
import no.nav.hjelpemidler.database.kotliquery.createSession
import javax.sql.DataSource

private val log = KotlinLogging.logger {}

interface Transaction<T : Any> {
    suspend operator fun <R> invoke(block: suspend T.() -> R): R
}

/**
 * Opprett [JdbcOperations] og start transaksjon eller gjenbruk eksisterende.
 *
 * Tilsvarer `PROPAGATION_REQUIRED` i Spring.
 *
 * Tillater suspending functions i transaksjonen for nettverkskall etc.
 *
 * NB! Ikke gjør parallelle kall med [JdbcOperations] i [block] som f.eks.:
 * ```kotlin
 * transaction { tx ->
 *     coroutineScope {
 *         launch { tx... }
 *         val deferred = async { tx... }
 *     }
 * }
 * ```
 *
 * @see [JdbcTransactionContext]
 * @see <a href="https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/tx-propagation.html#tx-propagation-required">Understanding PROPAGATION_REQUIRED</a>
 */
suspend fun <T> transaction(
    dataSource: DataSource,
    readOnly: Boolean = false,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (JdbcOperations) -> T,
): T {
    val properties = SessionProperties(
        readOnly = readOnly,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    )
    val context = currentTransactionContext() ?: return withTransactionContext(
        closeable = createSession(dataSource, properties),
    ) { session ->
        log.trace { "Establishing new database transaction, $properties" }
        session.transaction {
            val tx = SessionJdbcOperations(it)
            withContext(JdbcTransactionContext(properties, tx)) {
                block(tx)
            }
        }
    }
    log.trace { "Existing database transaction is reused, ${context.properties}" }
    if (properties != context.properties) {
        log.debug { "Session properties changed. Ignoring modifications in nested transaction because the outer transaction is reused, outer: (${context.properties}), nested: ($properties)" }
    }
    return block(context.tx)
}
