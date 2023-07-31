package no.nav.hjelpemidler.database

import kotliquery.Session
import javax.sql.DataSource

interface TransactionContextFactory<X : TransactionContext> {
    val dataSource: DataSource

    operator fun invoke(session: Session): X
}
