package no.nav.hjelpemidler.configuration

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.http.openid.azureADEnvironmentConfiguration
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `Skal lese konfigurasjon fra properties`() {
        val configuration = azureADEnvironmentConfiguration()
        configuration.tokenEndpoint shouldBe "http://azure/token"
        NaisEnvironmentVariable.NAIS_CLUSTER_NAME shouldBe "local"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som heltall`() {
        val PORT by environmentVariable<Int>()
        PORT shouldBe 8080
    }

    @Test
    fun `Skal kunne lese miljøvariabel som boolsk`() {
        val ENABLED by environmentVariable<Boolean>()
        ENABLED.shouldBeTrue()
    }

    @Test
    fun `Skal ikke feile ved valgfri miljøvariabel`() {
        shouldNotThrowAny {
            val FOOBAR by environmentVariable<String?>()
            FOOBAR.shouldBeNull()
        }
    }
}
