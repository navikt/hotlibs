package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ConfigurationTest {
    @Test
    fun `Skal lese konfigurasjon fra properties`() {
        Configuration["PORT"] shouldBe "8080"
    }

    @Test
    fun `Feiler ikke hvis manglende properties`() {
        val configuration = Configuration.load(GcpEnvironment.DEV)
        configuration shouldNotBe null
    }

    @Test
    fun `Miljøvariabler overstyrer properties`() {
        Configuration["TEST_CONFIGURATION_ENV_VAR_OVER_PROPERTIES"] shouldNotBe "9000"
        Configuration["TEST_CONFIGURATION_ENV_VAR_OVER_PROPERTIES"] shouldBe "9001"
    }
}
