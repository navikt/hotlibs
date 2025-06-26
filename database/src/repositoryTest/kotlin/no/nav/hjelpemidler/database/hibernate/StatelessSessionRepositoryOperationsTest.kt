package no.nav.hjelpemidler.database.hibernate

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.hibernate.test.Sakstype
import no.nav.hjelpemidler.database.hibernate.test.TestSakEntity
import no.nav.hjelpemidler.database.hibernate.test.TestSaksstatus
import no.nav.hjelpemidler.database.hibernate.test.testSessionFactory
import no.nav.hjelpemidler.database.repository.createNativeQuery
import no.nav.hjelpemidler.database.repository.findById
import no.nav.hjelpemidler.database.repository.transactionAsync
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import kotlin.test.Test

class StatelessSessionRepositoryOperationsTest {
    @Test
    fun `Skal lagre og hente saker`() = runTest {
        transactionAsync(testSessionFactory) { tx ->
            tx.insertAll(
                listOf(
                    TestSakEntity(
                        sakstype = Sakstype.SØKNAD,
                        fnr = Fødselsnummer(50.år),
                        saksstatuser = emptyList()
                    ),
                    TestSakEntity(
                        sakstype = Sakstype.BESTILLING,
                        fnr = Fødselsnummer(70.år),
                        saksstatuser = emptyList()
                    ),
                    TestSakEntity(
                        sakstype = Sakstype.BYTTE,
                        fnr = Fødselsnummer(90.år),
                        saksstatuser = emptyList()
                    ),
                )
            )

            val sakId1 = tx.insert(
                TestSakEntity(
                    sakstype = Sakstype.SØKNAD,
                    fnr = Fødselsnummer(50.år),
                    saksstatuser = emptyList()
                )
            ) as Long
            tx.insert(TestSaksstatus(sakId1, "OPPRETTET"))

            val sak1 = tx.findById<TestSakEntity>(sakId1)
            sak1.id shouldBe sakId1

            tx.fetch(sak1.saksstatuser)
            sak1.saksstatuser shouldHaveSize 1

            val ids = tx.createNativeQuery<Long>("SELECT id FROM sak_v1").resultList
            ids shouldHaveSize 4

            data class Result(val id: Long, val sakstype: String)

            val saker = tx.createNativeQuery<Result>("SELECT id, sakstype FROM sak_v1").resultList
            saker shouldHaveSize 4
        }
    }
}
