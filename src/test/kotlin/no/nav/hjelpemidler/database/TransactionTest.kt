package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.test.AbstractDatabaseTest
import no.nav.hjelpemidler.database.test.shouldBe
import kotlin.test.Test

class TransactionTest : AbstractDatabaseTest() {
    @Test
    fun `henter innslag med transaction context`() {
        val result = transaction(storeContext) { ctx ->
            ctx.testStore.hent(1)
        }
        result["id"] shouldBe 1L
    }
}
