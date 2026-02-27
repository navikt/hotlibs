package no.nav.hjelpemidler.database

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.kotliquery.SessionJdbcOperations
import no.nav.hjelpemidler.database.kotliquery.SessionProperties
import no.nav.hjelpemidler.database.kotliquery.createSession
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private val log = KotlinLogging.logger {}

interface Transaction<T : Any> {
    suspend operator fun <R> invoke(block: suspend T.() -> R): R
}

/**
 * Opprett [JdbcOperations] og start transaksjon eller gjenbruk eksisterende [JdbcOperations].
 *
 * Tilsvarer `PROPAGATION_REQUIRED` i Spring.
 *
 * Tillater suspending functions i transaksjonen for nettverkskall etc.
 *
 * NB! Ikke gjør parallelle kall med samme [JdbcOperations] i [block] som f.eks.:
 * ```kotlin
 * transaction { operations ->
 *     coroutineScope {
 *         launch { operations... }
 *         val deferred = async { operations... }
 *     }
 * }
 * ```
 *
 * NB! Parametre som [readOnly] etc. kan ikke endres i nestede transaksjoner siden transaksjonen gjenbrukes.
 * Eventuelle endringer vil bli ignorert.
 *
 * @see [TransactionJdbcOperations]
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
    val outer = currentCoroutineContext()[TransactionJdbcOperations] ?: return withContext(Dispatchers.IO) {
        log.trace { "Oppretter ny databasetransaksjon, $properties" }
        createSession(dataSource, properties).use { session ->
            session.transaction { transactionalSession ->
                val operations = SessionJdbcOperations(transactionalSession)
                withContext(TransactionJdbcOperations(properties, operations)) {
                    block(operations)
                }
            }
        }
    }
    log.trace { "Gjenbruker eksisterende databasetransaksjon, ${outer.properties}" }
    if (properties != outer.properties) {
        log.debug { "Transaksjonen ble forsøkt endret, men endringene ignoreres siden transaksjonen gjenbrukes, ytre: (${outer.properties}), indre: ($properties)" }
    }
    return block(outer.operations)
}

internal class TransactionJdbcOperations(
    val properties: SessionProperties,
    val operations: JdbcOperations,
) : AbstractCoroutineContextElement(TransactionJdbcOperations) {
    companion object Key : CoroutineContext.Key<TransactionJdbcOperations>
}
