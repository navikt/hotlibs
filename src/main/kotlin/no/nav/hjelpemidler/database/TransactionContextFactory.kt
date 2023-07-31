package no.nav.hjelpemidler.database

import kotliquery.Session
import java.sql.Connection

interface TransactionContextFactory<X : TransactionContext> {
    val connection: Connection

    operator fun invoke(session: Session): X
}
