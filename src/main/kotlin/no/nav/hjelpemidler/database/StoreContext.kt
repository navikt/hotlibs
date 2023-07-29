package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import javax.sql.DataSource

interface StoreContext<X : Any> {
    val dataSource: DataSource

    fun createTransactionContext(session: TransactionalSession): X

    operator fun invoke(session: TransactionalSession): X = createTransactionContext(session)
}
