package no.nav.hjelpemidler.database

import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.jdbc.JdbcTransactionContext
import no.nav.hjelpemidler.database.jdbc.SessionJdbcOperations
import no.nav.hjelpemidler.database.jdbc.createSession
import no.nav.hjelpemidler.database.jdbc.currentTransactionContext
import javax.sql.DataSource

interface Transaction<T : Any> {
    suspend operator fun <R> invoke(block: suspend T.() -> R): R
}

/**
 * Opprett [JdbcOperations] og start transaksjon.
 *
 * Tillater ikke suspending functions i transaksjonen og støtter ikke nestede transaksjoner.
 *
 * Bruk [transactionAsync] for å gjøre nettverkskall etc. i transaksjonen.
 *
 * @see [transactionAsync]
 */
suspend fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: (JdbcOperations) -> T,
): T = withTransactionContext(createSession(dataSource, returnGeneratedKeys, strict, queryTimeout)) { session ->
    session.transaction { block(SessionJdbcOperations(it)) }
}

/**
 * Opprett [JdbcOperations] og start transaksjon eller gjenbruk eksisterende.
 *
 * Tillater suspending functions i transaksjonen for nettverkskall etc.
 *
 * Bruk [transaction] hvis du ikke trenger å gjøre nettverkskall etc. i transaksjonen.
 *
 * @see [transaction]
 * @see [JdbcTransactionContext]
 */
suspend fun <T> transactionAsync(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (JdbcOperations) -> T,
): T {
    val context = currentTransactionContext() ?: return withTransactionContext(
        createSession(dataSource, returnGeneratedKeys, strict, queryTimeout)
    ) { session ->
        session.transaction {
            val tx = SessionJdbcOperations(it)
            withContext(JdbcTransactionContext(tx)) {
                block(tx)
            }
        }
    }
    return block(context.tx)
}
