package no.nav.hjelpemidler.domain.geografi

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

class KommuneTest {
    @Test
    fun `Kommuner med likt nummer er like`() {
        Kommune("1234", "A") shouldBe Kommune("1234", "B")
    }

    @Test
    fun `Kommuner med ulikt nummer er ulike`() {
        Kommune("0001", "A") shouldNotBe Kommune("0002", "A")
    }

    @TestFactory
    fun `Kommunenummer består av fire siffer`() = testFactory(
        listOf(
            "",
            "    ",
            "999",
            "99999",
            "\t9999\t",
            "abcd",
            "ABCD",
        ),
        { "Kommunenummer: '$it' er ugyldig" },
    ) {
        shouldThrow<IllegalArgumentException> { Kommune(it, "") }
    }

    @Test
    @Ignore("Brukes til å sjekke at alle definerte kommuner har riktig navn og nummer")
    fun `Valider kommuner`() {
        val kommuner = Kommune.Companion::class.declaredMemberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .mapNotNull { it.get(Kommune.Companion) as? Kommune }

        val client = HttpClient.newHttpClient()
        kommuner.forAll {
            val request = HttpRequest
                .newBuilder(URI("https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/${it.nummer}"))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            response.statusCode() shouldBe 200

            val body = response.body()
            body.shouldContainJsonKeyValue("kommunenummer", it.nummer)
            body.shouldContainJsonKeyValue("kommunenavn", it.navn)
        }
    }
}
