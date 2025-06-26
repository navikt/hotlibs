package no.nav.hjelpemidler.database.hibernate

import org.hibernate.Transaction
import org.hibernate.TransactionManagementException

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
