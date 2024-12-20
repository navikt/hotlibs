package no.nav.hjelpemidler.configuration

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uri.shouldHaveHost
import io.kotest.matchers.url.shouldHaveHost
import java.net.URI
import java.net.URL
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `Skal kunne lese miljøvariabel som Boolean`() {
        val ENABLED by environmentVariable<Boolean>()
        ENABLED.shouldBeTrue()
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Int`() {
        val PORT by environmentVariable<Int>()
        PORT shouldBe 8080
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URI`() {
        val API_URI by environmentVariable<URI>()
        API_URI shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URL`() {
        val API_URL by environmentVariable<URL>()
        API_URL shouldHaveHost "test"
    }

    @Test
    fun `Skal ikke feile ved valgfri miljøvariabel`() {
        shouldNotThrowAny {
            val FOOBAR by environmentVariable<String?>()
            FOOBAR.shouldBeNull()
        }
    }
}
