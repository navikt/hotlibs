package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.configuration.BehovsmeldingApplicationId
import no.nav.hjelpemidler.configuration.HotsakApplicationId
import no.nav.hjelpemidler.configuration.ResourceId
import java.util.UUID
import kotlin.test.Test

class URNTest {
    @Test
    fun `URN skal ha riktig NID og NSS`() {
        assertSoftly(URN("hotsak", "vedtak", "2400")) {
            nid shouldBe "hotsak"
            nss shouldBe "vedtak:2400"
            toURI().scheme shouldBe URN.SCHEME
        }
    }

    @Test
    fun `URN for Hotsak skal ha riktige verdier`() {
        val saksnotatId = "1200"
        assertSoftly(URN(HotsakApplicationId, SaksnotatResourceId, saksnotatId)) {
            application shouldBe HotsakApplicationId.application
            resource shouldBe SaksnotatResourceId.resource
            id shouldBe saksnotatId

            toString() shouldBe "urn:hotsak:saksnotat:$saksnotatId"

            validerEksternReferanseId()
        }
    }

    @Test
    fun `URN for digital behovsmelding skal ha riktige verdier`() {
        val behovsmeldingId = UUID.randomUUID()
        assertSoftly(URN(BehovsmeldingApplicationId, BehovsmeldingResourceId, behovsmeldingId.toString())) {
            application shouldBe BehovsmeldingApplicationId.application
            resource shouldBe BehovsmeldingResourceId.resource
            id shouldBe behovsmeldingId.toString()

            toString() shouldBe "urn:behovsmelding:behovsmelding:$behovsmeldingId"

            validerEksternReferanseId()
        }
    }

    private data object SaksnotatResourceId : ResourceId {
        override val resource: String get() = "saksnotat"
    }

    private data object BehovsmeldingResourceId : ResourceId {
        override val resource: String get() = "behovsmelding"
    }
}
