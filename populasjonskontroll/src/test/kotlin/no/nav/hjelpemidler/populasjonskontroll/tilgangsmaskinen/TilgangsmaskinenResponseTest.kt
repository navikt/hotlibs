package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import kotlin.test.Test

class TilgangsmaskinenResponseTest {
    @Test
    fun `Skal håndtere generell feil fra tilgangsmaskinen`() {
        val details = jsonToValue<TilgangsmaskinenResponse.Avvist>(
            """
                {
                  "detail": "Forventet token type OBO for /api/v1/kjerne, fikk CCF",
                  "instance": "/api/v1/kjerne",
                  "status": 403,
                  "title": "Forbidden"
                }
            """.trimIndent(),
        )
        details.status shouldBe HttpStatusCode.Forbidden
    }
}
