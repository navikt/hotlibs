package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.configuration.BehovsmeldingApplicationId
import no.nav.hjelpemidler.configuration.HotsakApplicationId
import kotlin.test.Test

class EksternIdTest {
    @Test
    fun `EksternId for Hotsak skal ha riktige verdier`() {
        eksternIdHotsak.application shouldBe HotsakApplicationId.application
        eksternIdHotsak.resource shouldBe "saksnotat"
        eksternIdHotsak.id shouldBe "1"

        eksternIdHotsak.toString() shouldBe "urn:hotsak:saksnotat:1"
    }

    @Test
    fun `EksternId for digital behovsmelding skal ha riktige verdier`() {
        eksternIdBehovsmelding.application shouldBe BehovsmeldingApplicationId.application
        eksternIdBehovsmelding.resource shouldBe "behovsmelding"
        eksternIdBehovsmelding.id shouldBe behovsmeldingId.toString()

        eksternIdBehovsmelding.toString() shouldBe "urn:behovsmelding:behovsmelding:$behovsmeldingId"
    }
}
