package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import javax.sql.DataSource

interface StoreContext<X : TransactionContext> {
    val dataSource: DataSource

    fun transactionContext(tx: TransactionalSession): X
}
