package no.nav.hjelpemidler.domain.enhet

import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.test.Ignore
import kotlin.test.Test

class EnhetTest {
    @Test
    fun `Enheter med likt nummer er like`() {
        val enhetA = Enhet("1234", "A")
        val enhetB = Enhet(enhetA.nummer, "B")

        enhetA shouldBe enhetB

        enhetA.compareTo(enhetB) shouldBe 0
    }

    @Test
    fun `Enheter med ulikt nummer er ulike`() {
        val enhetA = Enhet("0001", "A")
        val enhetB = Enhet("0002", enhetA.navn)

        enhetA shouldNotBe enhetB

        enhetA.compareTo(enhetB) shouldNotBe 0
    }

    @Test
    fun `Ulike underklasser av Enhet er ulike`() {
        val enhetA = object : Enhet("2050", "A") {}
        val enhetB = object : Enhet(enhetA) {}

        enhetA shouldNotBe enhetB

        enhetA.compareTo(enhetB) shouldNotBe 0
    }

    @TestFactory
    fun `Enhetsnummer består av fire siffer`() = testFactory(
        sequenceOf(
            "",
            "    ",
            "999",
            "99999",
            "\t9999\t",
            "abcd",
            "ABCD",
        ),
        { "Enhetsnummer: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException> { Enhet(it, "") }
    }

    @Test
    @Ignore("Brukes til å sjekke at alle definerte enheter har riktig navn og nummer")
    fun `Valider enheter`() {
        val enheter = Enhet.Companion::class.declaredMemberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .mapNotNull { it.get(Enhet.Companion) as? Enhet }

        val client = HttpClient.newHttpClient()
        enheter.forAll {
            val request = HttpRequest
                .newBuilder(URI("https://norg2.intern.nav.no/norg2/api/v1/enhet/${it.nummer}"))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            response.statusCode() shouldBe 200

            val body = response.body()
            body.shouldContainJsonKeyValue("enhetNr", it.nummer)
            body.shouldContainJsonKeyValue("navn", it.navn)
        }
    }
}
