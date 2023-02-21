package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.Test

class EnvironmentTest {
    @Test
    fun `gjeldende miljø skal være local`() {
        Environment.current() shouldBe LocalEnvironment
    }
}
