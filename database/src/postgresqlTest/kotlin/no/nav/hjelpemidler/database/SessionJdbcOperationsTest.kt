package no.nav.hjelpemidler.database

import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestStore
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.test.testDatabase
import no.nav.hjelpemidler.database.test.toTestEntity
import no.nav.hjelpemidler.domain.person.Personnavn
import kotlin.test.Test

class SessionJdbcOperationsTest {
    @Test
    fun `Henter ett innslag`() = runTest {
        val nyEntity = TestEntity()
        val id = testDatabase { lagre(nyEntity) }
        val lagretEntity = testDatabase { hent(id) }
        lagretEntity shouldBe nyEntity.copy(id = id)
    }

    @Test
    fun `Henter flere innslag`() = runTest {
        val ids = lagreEntities(10)
        val result = testDatabase { hent(ids).map { it.id.value } }

        result shouldBe ids
    }

    @Test
    fun `Henter side`() = runTest {
        val ids = lagreEntities(20)
        val result = transactionAsync(testDataSource) { tx ->
            tx.page(
                sql = "SELECT *, COUNT(1) OVER() AS total_elements FROM test WHERE id = ANY (:ids) ORDER BY id",
                queryParameters = mapOf("ids" to ids.toTypedArray()),
                pageRequest = PageRequest(1, 5),
                mapper = Row::toTestEntity,
            )
        }

        result shouldHaveSize 5
        result.totalElements shouldBe ids.size
        result.map { it.id.value } shouldBe ids.subList(0, 5)
    }

    @Test
    fun `Henter JSON`() = runTest {
        val data1 = mapOf("key" to "value")
        val id = testDatabase { lagre(TestEntity(data1 = data1)) }
        val result = testDatabase { hentMap(id, "id", "data_1") }

        result.shouldContain("id", id.value)
        result.shouldContain("data_1", pgObjectOf("jsonb", """{"key": "value"}"""))
    }

    @Test
    fun `Oppdaterer innslag`() = runTest {
        val id = testDatabase { lagre(TestEntity()) }
        val result = testDatabase { oppdater(id, 50) }

        shouldNotThrow<IllegalStateException> { result.expect(expectedRowCount = 1) }
    }

    @Test
    fun `Sletter innslag`() = runTest {
        val id = testDatabase { lagre(TestEntity()) }
        val result = testDatabase { slett(id) }

        result shouldBe false
    }

    @Test
    fun `Setter inn flere innslag`() = runTest {
        val items = listOf(
            TestEntity(string = "x1", integer = 1, enum = TestEnum.A, data1 = mapOf("key" to "t1")),
            TestEntity(string = "x2", integer = 2, enum = TestEnum.B, data1 = mapOf("key" to "t2")),
            TestEntity(string = "x3", integer = 3, enum = TestEnum.C, data1 = mapOf("key" to "t3")),
        )

        val rows1 = transactionAsync(testDataSource) { tx ->
            tx.batch(TestStore.SQL_INSERT, items, TestEntity::toQueryParameters)
        }

        rows1 shouldHaveSize 3

        val rows2 = transactionAsync(testDataSource) { tx ->
            tx.list("SELECT * FROM test WHERE string_1 LIKE 'x%'", mapper = Row::toTestEntity)
        }

        rows2 shouldHaveSize 3
    }

    @Test
    fun `Skal sette inn og hente null som null`() = runTest {
        val id = testDatabase { lagre(TestEntity(data2 = null)) }
        val result = testDatabase { hentMap(id, "id", "data_2") }

        result.shouldContain("id", id.value)
        result.shouldContain("data_2", null)
    }

    @Test
    fun `Skal sette inn og hente NullNode som null`() = runTest {
        val id = testDatabase { lagre(TestEntity(data2 = NullNode.getInstance())) }
        val result = testDatabase { hentMap(id, "id", "data_2") }

        result.shouldContain("id", id.value)
        result.shouldContain("data_2", null)
    }

    @Test
    fun `Skal sette inn og hente MissingNode som null`() = runTest {
        val id = testDatabase { lagre(TestEntity(data2 = MissingNode.getInstance())) }
        val result = testDatabase { hentMap(id, "id", "data_2") }

        result.shouldContain("id", id.value)
        result.shouldContain("data_2", null)
    }

    @Test
    fun `Skal hente som JsonNode`() = runTest {
        val id = testDatabase {
            lagre(
                TestEntity(
                    string = "string",
                    integer = 1000,
                    data1 = mapOf("k1" to 1, "k2" to "2"),
                    navn = Personnavn("Fornavn", null, "Etternavn")
                )
            )
        }
        val result = testDatabase { hentTree(id) }.shouldBeInstanceOf<ObjectNode>()
        result["id"]?.longValue() shouldBe id.value
    }

    private suspend fun lagreEntities(antall: Int): List<Long> = testDatabase {
        lagre((1..antall).map { TestEntity(integer = it) })
    }
}
