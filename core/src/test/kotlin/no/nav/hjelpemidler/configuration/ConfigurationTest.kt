package no.nav.hjelpemidler.configuration

import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ConfigurationTest {
    @Test
    fun `Skal lese konfigurasjon fra properties`() {
        val configuration = Configuration.load(TestEnvironment)
        configuration["PORT"] shouldBe "8080"
    }

    @Test
    fun `Feiler ikke hvis manglende properties`() {
        val configuration = Configuration.load(GcpEnvironment.DEV)
        configuration shouldNotBe null
    }

    @Test
    fun `Env overstyrer properties`() {
        withEnvironment("PORT", "8081") {
            val configuration = Configuration.load(TestEnvironment)
            configuration["PORT"] shouldNotBe "8080"
            configuration["PORT"] shouldBe "8081"
        }
    }
}
