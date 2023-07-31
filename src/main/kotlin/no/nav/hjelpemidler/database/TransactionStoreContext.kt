package no.nav.hjelpemidler.database

import kotliquery.Session
import java.sql.Connection

interface TransactionStoreContext<X : Any> {
    val connection: Connection

    suspend fun <T> execute(block: suspend (X) -> T): T

    fun createTransactionContext(tx: Session): X

    operator fun invoke(tx: Session): X = createTransactionContext(tx)
}
