package no.nav.hjelpemidler.database.hibernate

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.SharedSessionContract
import org.hibernate.StatelessSession

/**
 * @see [org.hibernate.internal.TransactionManagement.manageTransaction]
 */
internal inline fun <T : SharedSessionContract, R> T.transaction(block: (T) -> R): R {
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
    openSession().use { session ->
        session.transaction<Session, T> { block(it) }
    }

/**
 * @see [org.hibernate.Session]
 * @see [org.hibernate.SessionFactory.fromStatelessTransaction]
 */
suspend fun <T> SessionFactory.statelessTransaction(block: suspend (StatelessSession) -> T): T =
    openStatelessSession().use { session ->
        session.transaction<StatelessSession, T> { block(it) }
    }
