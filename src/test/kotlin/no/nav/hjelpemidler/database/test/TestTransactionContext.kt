package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.TransactionContext

interface TestTransactionContext : TransactionContext {
    val testStore: TestStore
}
