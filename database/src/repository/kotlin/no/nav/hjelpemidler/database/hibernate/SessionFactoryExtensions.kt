package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.withTransactionContext
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.SharedSessionContract
import org.hibernate.StatelessSession

/**
 * @see [org.hibernate.internal.TransactionManagement.manageTransaction]
 */
internal inline fun <S : SharedSessionContract, T> S.transaction(block: (S) -> T): T {
    val transaction = beginTransaction()
    return try {
        val result = block(this)
        commit(transaction)
        result
    } catch (e: RuntimeException) {
        rollback(transaction, e)
        throw e
    }
}

/**
 * @see [org.hibernate.Session]
 * @see [org.hibernate.SessionFactory.fromTransaction]
 */
suspend fun <T> SessionFactory.transaction(block: suspend (Session) -> T): T =
    withTransactionContext(openSession()) { session ->
        session.transaction<Session, T> { block(it) }
    }

/**
 * @see [org.hibernate.StatelessSession]
 * @see [org.hibernate.SessionFactory.fromStatelessTransaction]
 */
suspend fun <T> SessionFactory.statelessTransaction(block: suspend (StatelessSession) -> T): T =
    withTransactionContext(openStatelessSession()) { session ->
        session.transaction<StatelessSession, T> { block(it) }
    }
