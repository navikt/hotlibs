package no.nav.hjelpemidler.pip.skjerming

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.http.openid.TokenSet
import no.nav.hjelpemidler.test.ktor.StubEngine
import no.nav.hjelpemidler.test.ktor.baseUrl
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours

class SkjermedePersonerPipClientTest {
    private val engine = StubEngine {
        baseUrl(SKJERMEDE_PERSONER_PIP_API_URL) {
            routing {
                post("/skjermet") {
                    call.respond(true)
                }
                post("/skjermetBulk") {
                    val request = call.receive<ErSkjermedePersonerRequest>()
                    call.respond(request.fnr.associateWith { true })
                }
            }
        }
    }
    private val client = SkjermedePersonerPipClient(
        engine = engine,
        tokenSetProvider = TokenSet("token", 1.hours).asTokenSetProvider(),
    )

    @Test
    fun `Skal hente person fra skjermede-personer-pip-api`() = runTest {
        val id = Fødselsnummer(60.år)
        val response = client.hentErSkjermetPerson(id)
        response.shouldBeTrue()
    }

    @Test
    fun `Skal hente personer fra skjermede-personer-pip-api`() = runTest {
        val ider = setOf(
            Fødselsnummer(40.år),
            Fødselsnummer(70.år),
        )
        val personer = client.hentErSkjermedePersoner(ider)
        personer.keys shouldBe ider
    }
}
