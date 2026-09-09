package no.nav.hjelpemidler.pip.pdl

import io.kotest.matchers.shouldBe
import io.ktor.server.request.receive
import io.ktor.server.request.requireHeader
import io.ktor.server.response.respond
import io.ktor.server.routing.get
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

class PdlPipApiClientTest {
    private val engine = StubEngine {
        baseUrl(PDL_PIP_API_URL) {
            routing {
                get("/person") {
                    val fnr = call.requireHeader("ident").let(::Fødselsnummer)
                    call.respond(lagPipPersonResponse(fnr))
                }
                post("/personBolk") {
                    val ider = call.receive<Set<Fødselsnummer>>()
                    call.respond(ider.associateWith(::lagPipPersonResponse))
                }
            }
        }
    }
    private val client = PdlPipApiClient(
        engine = engine,
        tokenSetProvider = TokenSet("token", 1.hours).asTokenSetProvider(),
    )

    @Test
    fun `Skal hente person fra pdl-pip-api`() = runTest {
        val id = Fødselsnummer(60.år)
        val response = client.hentPerson(id)
        response.fnr shouldBe id
    }

    @Test
    fun `Skal hente personer fra pdl-pip-api`() = runTest {
        val ider = setOf(
            Fødselsnummer(40.år),
            Fødselsnummer(70.år),
        )
        val personer = client.hentPersoner(ider)
        personer.keys shouldBe ider
    }
}
