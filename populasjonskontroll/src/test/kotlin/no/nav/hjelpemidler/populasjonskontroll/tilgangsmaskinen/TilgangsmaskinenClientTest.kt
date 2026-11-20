package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.populasjonskontroll.pdl.lagPipPersonResponse
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenBulkResponse.Resultat
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenResponse.Avvist
import no.nav.hjelpemidler.security.AutentisertAnsatt
import no.nav.hjelpemidler.test.ktor.StubEngine
import no.nav.hjelpemidler.test.ktor.baseUrl
import no.nav.hjelpemidler.test.ktor.texas
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

@Disabled
class TilgangsmaskinenClientTest {
    private val personUgradert = lagPipPersonResponse(Fødselsnummer(50.år))
    private val personStrengtFortrolig = lagPipPersonResponse(Fødselsnummer(60.år))

    private val ansatt = TestAutentisertAnsatt

    private val client = TilgangsmaskinenClient(
        engine = StubEngine {
            texas()
            baseUrl(TilgangsmaskinenClient.TILGANGSMASKINEN_BASE_URL) { baseUrl ->
                routing {
                    post("$baseUrl/api/v1/kjerne") {
                        val resultat: Avvist? = null
                        if (resultat == null) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.Forbidden, resultat)
                        }
                    }

                    post("$baseUrl/api/v1/bulk/obo/{regelType}") {
                        val personIder = call.receive<Set<PersonId>>()
                        call.respond(
                            TilgangsmaskinenBulkResponse(
                                ansattId = ansatt.id,
                                resultater = personIder.map { personId ->
                                    val detaljer: Avvist? = null
                                    if (detaljer == null) {
                                        Resultat.Godkjent(
                                            brukerId = personId,
                                            status = HttpStatusCode.NoContent,
                                        )
                                    } else {
                                        Resultat.Avvist(
                                            brukerId = personId,
                                            status = HttpStatusCode.Forbidden,
                                            detaljer = TilgangsmaskinenResponses.avvist(personId, ansatt.id),
                                        )
                                    }
                                },
                            ),
                        )
                    }
                }
            }
        },
    )

    @Test
    fun `Saksbehandler har tilgang til person med strengt fortrolig adresse`() = runTest {
        val personId = personStrengtFortrolig.fnr!!

        val response = client.evaluerKjernereglerForAnsatt(ansatt, personId)

        response.shouldBeInstanceOf<TilgangsmaskinenResponse.Godkjent>()
    }

    @Test
    fun `Saksbehandler har ikke tilgang til person med strengt fortrolig adresse`() = runTest {
        val personId = personStrengtFortrolig.fnr!!

        val response = client.evaluerKjernereglerForAnsatt(ansatt, personId)

        response.shouldBeInstanceOf<Avvist>()
        response.kode shouldBe Avvist.Kode.AVVIST_STRENGT_FORTROLIG_ADRESSE
        response.navIdent shouldBe ansatt.id
        response.brukerIdent shouldBe personId
    }

    @Test
    fun `Saksbehandler har kun tilgang til noen personer`() = runTest {
        val personIder = setOfNotNull(personUgradert.fnr, personStrengtFortrolig.fnr)
        val response = client.evaluerKjernereglerForAnsatt(ansatt, personIder = personIder)

        response shouldBeSameSizeAs personIder
        response.filterIsInstance<Resultat.Godkjent>().shouldBeSingleton {
            it.brukerId shouldBe personUgradert.fnr
        }
        response.filterIsInstance<Resultat.Avvist>().shouldBeSingleton {
            it.brukerId shouldBe personStrengtFortrolig.fnr
        }
    }
}

private object TestAutentisertAnsatt : AutentisertAnsatt {
    override val id: NavIdent = NavIdent.UKJENT
    override val navn: String = ""
    override val epost: String = ""
    override val grupper: Set<Gruppe> = emptySet()
    override val userToken: String = "userToken"

    @JvmInline
    value class Gruppe(override val id: UUID) : AutentisertAnsatt.Gruppe
}
