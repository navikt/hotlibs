package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ConfigurationTest {
    @Test
    fun `Skal lese konfigurasjon fra properties`() {
        val configuration = Configuration.load(LocalEnvironment)
        configuration["PORT"] shouldBe "8080"
    }

    @Test
    fun `Feiler ikke hvis manglende properties`() {
        val configuration = Configuration.load(GcpEnvironment.DEV)
        configuration shouldNotBe null
    }
}
