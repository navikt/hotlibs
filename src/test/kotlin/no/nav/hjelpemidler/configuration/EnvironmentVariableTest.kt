package no.nav.hjelpemidler.configuration

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.http.openid.azureADEnvironmentConfiguration
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `skal lese konfigurasjon fra properties`() {
        val configuration = azureADEnvironmentConfiguration()
        configuration.tokenEndpoint shouldBe "http://azure/token"
        NaisEnvironmentVariable.NAIS_CLUSTER_NAME shouldBe "local"
    }
}
