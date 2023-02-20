package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.http.openid.azureADEnvironmentConfiguration
import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `skal lese konfigurasjon fra properties`() {
        val configuration = azureADEnvironmentConfiguration()
        configuration.tokenEndpoint shouldBe "http://azure/token"
        NaisEnvironmentVariable.NAIS_CLUSTER_NAME shouldBe "local"
    }
}
