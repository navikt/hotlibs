package no.nav.hjelpemidler.joark

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class EksternReferanseIdTest {
    private val id = UUID.randomUUID()

    @Test
    fun `Id for behovsmelding til Gosys`() {
        val eksternReferanseId = EksternReferanseId.behovsmeldingGosys(id)

        eksternReferanseId.value shouldBe "urn:behovsmelding-gosys:$id"

        eksternReferanseId.søknadId shouldBe id
    }

    @Test
    fun `Id for behovsmelding til Hotsak`() {
        val eksternReferanseId = EksternReferanseId.behovsmeldingHotsak(id)

        eksternReferanseId.value shouldBe "urn:behovsmelding-hotsak:$id"

        eksternReferanseId.søknadId shouldBe id
    }
}
