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

interface Transaction {
    suspend operator fun <T> invoke(block: suspend context(TransactionContext) () -> T): T
}

/**
 * Opprett [TransactionContext] og start transaksjon eller gjenbruk eksisterende [TransactionContext].
 *
 * Tilsvarer `PROPAGATION_REQUIRED` i Spring.
 *
 * Tillater suspending functions i transaksjonen for nettverkskall etc.
 *
 * Funksjonen kan også brukes med Kotlin context parameters (tilgjengelig fra Kotlin 2.4.0):
 * [Context parameters | Kotlin Documentation](https://kotlinlang.org/docs/context-parameters.html)
 *
 * Eksempel på bruk:
 * ```kotlin
 * object SakRepository : Repository {
 *     context(ctx: TransactionContext)
 *     fun opprettSak(): Long {
 *         return ctx.single<Long>("INSERT INTO sak (sakstype) VALUES ('SØKNAD') RETURNING id")
 *     }
 * }
 *
 * class SakService(private val dataSource: DataSource) {
 *     suspend fun opprettSak() {
 *         transaction(dataSource) {
 *             val sakId = SakRepository.opprettSak()
 *             annenOperasjon(sakId) // denne kjøres i samme transaksjon
 *         }
 *     }
 *
 *     context(ctx: TransactionContext)
 *     private suspend fun annenOperasjon(sakId: Long) {
 *         ctx.execute("UPDATE saksgrunnlag SET sak_id = :sakId WHERE sak_id IS NULL", mapOf("sakId" to sakId))
 *     }
 * }
 * ```
 *
 * NB! Ikke gjør parallelle kall med samme [TransactionContext] i [block] som f.eks.:
 * ```kotlin
 * transaction { ctx ->
 *     coroutineScope {
 *         launch { ctx... }
 *         val deferred = async { ctx... }
 *     }
 * }
 * ```
 *
 * NB! Parametre som [readOnly] etc. kan ikke endres i nestede transaksjoner siden transaksjonen gjenbrukes.
 * Eventuelle endringer vil bli ignorert.
 *
 * @see <a href="https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/tx-propagation.html#tx-propagation-required">Understanding PROPAGATION_REQUIRED</a>
 */
suspend fun <T> transaction(
    dataSource: DataSource,
    readOnly: Boolean = false,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend context(TransactionContext) (TransactionContext) -> T,
): T {
    val sessionProperties = SessionProperties(
        readOnly = readOnly,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    )
    val outerContext = currentCoroutineContext()[CoroutineTransactionContext] ?: return withContext(Dispatchers.IO) {
        log.trace { "Oppretter ny databasetransaksjon, $sessionProperties" }
        createSession(dataSource, sessionProperties).use { session ->
            session.transaction { transactionalSession ->
                val jdbcOperations = SessionJdbcOperations(transactionalSession)
                val innerContext = CoroutineTransactionContext(sessionProperties, jdbcOperations)
                withContext(innerContext) {
                    block(innerContext, innerContext)
                }
            }
        }
    }
    log.trace { "Gjenbruker eksisterende databasetransaksjon, ${outerContext.sessionProperties}" }
    if (sessionProperties != outerContext.sessionProperties) {
        log.debug { "Transaksjonen ble forsøkt endret, men endringene ignoreres siden transaksjonen gjenbrukes, ytre: (${outerContext.sessionProperties}), indre: ($sessionProperties)" }
    }
    return block(outerContext, outerContext)
}

internal class CoroutineTransactionContext(
    val sessionProperties: SessionProperties,
    val jdbcOperations: JdbcOperations,
) : AbstractCoroutineContextElement(CoroutineTransactionContext), TransactionContext, JdbcOperations by jdbcOperations {
    companion object Key : CoroutineContext.Key<CoroutineTransactionContext>
}
