package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.Test

class ConfigurationTest {
    @Test
    fun `skal lese konfigurasjon fra properties`() {
        val configuration = Configuration("/local.properties")
        configuration["AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"] shouldBe "http://azure/token"
    }
}
