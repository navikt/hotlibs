package no.nav.hjelpemidler.database.hibernate

import org.hibernate.Transaction
import org.hibernate.TransactionManagementException

/**
 * @see [org.hibernate.internal.TransactionManagement.manageTransaction]
 */
internal inline fun <S : Any, T> manageTransaction(
    session: S,
    transaction: Transaction,
    block: (S) -> T,
): T {
    try {
        val result = block(session)
        commit(transaction)
        return result
    } catch (e: RuntimeException) {
        rollback(transaction, e)
        throw e
    }
}

/**
 * @see [org.hibernate.internal.TransactionManagement.rollback]
 */
internal fun rollback(transaction: Transaction, exception: RuntimeException) {
    if (transaction.isActive) {
        try {
            transaction.rollback()
        } catch (e: RuntimeException) {
            exception.addSuppressed(e)
        }
    }
}

/**
 * @see [org.hibernate.internal.TransactionManagement.commit]
 */
internal fun commit(transaction: Transaction) {
    if (!transaction.isActive) {
        throw TransactionManagementException("Execution of action caused managed transaction to be completed")
    }
    transaction.commit()
}
