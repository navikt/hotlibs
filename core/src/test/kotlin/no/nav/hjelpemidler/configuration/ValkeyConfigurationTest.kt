package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.text.toURI
import kotlin.test.Test

class ValkeyConfigurationTest {
    @Test
    fun `Skal opprette Valkey-konfigurasjon fra miljøvariabler`() {
        val configuration = ValkeyConfiguration("test")

        configuration.uri shouldBe "valkeys://10.0.0.1:9990".toURI()
        configuration.redisUri shouldBe "rediss://10.0.0.1:9990".toURI()
        configuration.host shouldBe "10.0.0.1"
        configuration.port shouldBe 9990
        configuration.username shouldBe "username"
        configuration.password shouldBe "password"
    }

    @Test
    fun `Skal opprette default Valkey-konfigurasjon`() {
        val configuration = ValkeyConfiguration("ukjent")

        configuration.uri shouldBe "valkey://localhost:6379".toURI()
        configuration.redisUri shouldBe "redis://localhost:6379".toURI()
        configuration.host shouldBe "localhost"
        configuration.port shouldBe 6379
        configuration.username shouldBe null
        configuration.password shouldBe null
    }
}
