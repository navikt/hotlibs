package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.configuration.ValkeyConfiguration.Provider
import no.nav.hjelpemidler.text.toURI
import kotlin.test.Test

class ValkeyConfigurationTest {
    @Test
    fun `Skal opprette Valkey-konfigurasjon fra miljøvariabler`() {
        val configuration = ValkeyConfiguration("test", Provider.VALKEY)

        configuration.uri shouldBe "valkeys://10.0.0.1:26483".toURI()
        configuration.uri.host shouldBe "10.0.0.1"
        configuration.uri.port shouldBe 26483
        configuration.username shouldBe "username"
        configuration.password shouldBe "password"
    }

    @Test
    fun `Skal opprette default Valkey-konfigurasjon`() {
        val configuration = ValkeyConfiguration("ukjent", Provider.REDIS)

        configuration.uri shouldBe "redis://localhost:6379".toURI()
        configuration.uri.host shouldBe "localhost"
        configuration.uri.port shouldBe 6379
        configuration.username shouldBe null
        configuration.password shouldBe null
    }
}
