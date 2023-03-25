package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import javax.sql.DataSource

interface StoreContext<X : Any> {
    val dataSource: DataSource

    fun createTransactionContext(tx: TransactionalSession): X

    operator fun invoke(tx: TransactionalSession): X =
        createTransactionContext(tx)
}
