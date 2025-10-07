package no.nav.hjelpemidler.database.hibernate

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.persistence.Tuple
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.hibernate.test.Sakstype
import no.nav.hjelpemidler.database.hibernate.test.TestSakEntity
import no.nav.hjelpemidler.database.hibernate.test.TestSaksstatusEntity
import no.nav.hjelpemidler.database.hibernate.test.testSessionFactory
import no.nav.hjelpemidler.database.repository.createNativeQuery
import no.nav.hjelpemidler.database.repository.findById
import no.nav.hjelpemidler.database.repository.single
import no.nav.hjelpemidler.database.repository.toRecord
import no.nav.hjelpemidler.database.repository.transactionAsync
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import java.time.Instant
import kotlin.test.Test

class StatelessSessionRepositoryOperationsTest {
    @Test
    fun `Skal lagre og hente saker`() = runTest {
        transactionAsync(testSessionFactory) { tx ->
            val fnr = Fødselsnummer(50.år)
            tx.insertAll(
                listOf(
                    TestSakEntity(
                        sakstype = Sakstype.SØKNAD,
                        fnr = fnr,
                    ),
                    TestSakEntity(
                        sakstype = Sakstype.BESTILLING,
                        fnr = fnr,
                    ),
                    TestSakEntity(
                        sakstype = Sakstype.BYTTE,
                        fnr = fnr,
                    ),
                )
            )

            val sakId1 = tx.insert(
                TestSakEntity(
                    sakstype = Sakstype.SØKNAD,
                    fnr = fnr,
                )
            ) as Long
            tx.insert(TestSaksstatusEntity(sakId1, "OPPRETTET"))

            val sak1 = tx.findById<TestSakEntity>(sakId1).shouldNotBeNull()
            sak1.id shouldBe sakId1

            tx.fetch(sak1.saksstatuser)
            sak1.saksstatuser shouldHaveSize 1

            val ids = tx.createNativeQuery<Long>("SELECT id FROM sak_v1 WHERE fnr = :fnr")
                .apply { setParameter("fnr", fnr.value) }
                .resultList
            ids shouldHaveSize 4

            data class Sak(
                val id: Long,
                val sakstype: String,
            )

            val saker = tx.createNativeQuery<Sak>("SELECT id, sakstype FROM sak_v1 WHERE fnr = :fnr")
                .apply { setParameter("fnr", fnr.value) }
                .resultList
            saker shouldHaveSize 4
        }
    }

    @Test
    fun `Skal lagre og hente saker med native query`() = runTest {
        transactionAsync(testSessionFactory) { tx ->
            val sakId = tx.insert(
                TestSakEntity(
                    sakstype = Sakstype.SØKNAD,
                    fnr = Fødselsnummer(50.år),
                )
            ) as Long
            tx.insert(TestSaksstatusEntity(sakId, "OPPRETTET"))

            data class Sak(
                val id: Long,
                val sakstype: String,
                val opprettet: Instant,
            )

            val sak = tx.single<Sak>(
                sql = "SELECT id, sakstype, opprettet FROM sak_v1 WHERE id = :id",
                queryParameters = mapOf("id" to sakId),
            )

            sak.id shouldBe sakId
            sak.sakstype shouldBe Sakstype.SØKNAD.toString()
            sak.opprettet.shouldNotBeNull()

            val sakRecord = tx.single<Tuple>(
                sql = "SELECT id, sakstype, saksgrunnlag, opprettet, NULL AS oppdatert FROM sak_v1 WHERE id = :id",
                queryParameters = mapOf("id" to sakId),
            ).toRecord()
            sakRecord.get<Long>("id") shouldBe sakId
        }
    }
}
