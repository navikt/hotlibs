package no.nav.hjelpemidler.database

import kotliquery.Session

interface StoreContext<T> {
    fun transactionContext(tx: Session): T
}
