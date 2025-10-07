package no.nav.hjelpemidler.database.hibernate

import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.hibernate.test.Sakstype
import no.nav.hjelpemidler.database.hibernate.test.TestSakEntity
import no.nav.hjelpemidler.database.hibernate.test.testSessionFactory
import no.nav.hjelpemidler.database.repository.RepositoryOperations
import no.nav.hjelpemidler.database.repository.createRepository
import no.nav.hjelpemidler.database.repository.transactionAsync
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import org.junit.jupiter.api.Test

class StatelessSessionRepositoryTest {
    @Test
    fun `Skal lagre og hente saker`() = runTest {
        val sakId1 = transactionAsync(testSessionFactory) { tx ->
            val repository = tx.sakRepository

            val sakId1 = repository.insert(
                TestSakEntity(
                    sakstype = Sakstype.SØKNAD,
                    fnr = Fødselsnummer(50.år),
                )
            )
            val sakId2 = repository.insert(
                TestSakEntity(
                    sakstype = Sakstype.BESTILLING,
                    fnr = Fødselsnummer(60.år),
                )
            )

            val saker = repository.findAllById(setOf(sakId1, sakId2)).filterNotNull()
            saker shouldHaveSize 2
            saker.forOne { it.id shouldBe sakId1 }
            saker.forOne { it.id shouldBe sakId2 }

            sakId1
        }

        transactionAsync(testSessionFactory) { tx ->
            val repository = tx.sakRepository

            repository.upsert(
                TestSakEntity(
                    sakstype = Sakstype.BESTILLING,
                    fnr = Fødselsnummer(55.år),
                    id = sakId1
                )
            )

            val sak1 = repository.findById(sakId1).shouldNotBeNull()
            sak1.sakstype shouldBe Sakstype.BESTILLING
        }
    }
}

private val RepositoryOperations.sakRepository
    get() = createRepository<TestSakEntity, Long>()
