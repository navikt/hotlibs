package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class EnvironmentTest {
    @Test
    fun `gjeldende miljø skal være local`() {
        Environment.current shouldBe LocalEnvironment
    }
}
