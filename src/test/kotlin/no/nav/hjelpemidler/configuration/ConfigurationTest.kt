package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ConfigurationTest {
    @Test
    fun `skal lese konfigurasjon fra properties`() {
        val configuration = Configuration.load(LocalEnvironment)
        configuration["AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"] shouldBe "http://azure/token"
    }

    @Test
    fun `feiler ikke hvis manglende properties`() {
        val configuration = Configuration.load(GcpEnvironment.LABS)
        configuration shouldNotBe null
    }
}
