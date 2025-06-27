package no.nav.hjelpemidler.configuration

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uri.shouldHaveHost
import io.kotest.matchers.url.shouldHaveHost
import no.nav.hjelpemidler.function.identity
import no.nav.hjelpemidler.text.toURI
import no.nav.hjelpemidler.text.toURL
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `Skal kunne lese miljøvariabel som Boolean`() {
        val ENABLED by EnvironmentVariable(transform = String::toBoolean)
        ENABLED.shouldBeTrue()
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Int`() {
        val PORT by EnvironmentVariable(transform = String::toInt)
        PORT shouldBe 8080
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URI`() {
        val API_URI by EnvironmentVariable(transform = String::toURI)
        API_URI shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URL`() {
        val API_URL by EnvironmentVariable(transform = String::toURL)
        API_URL shouldHaveHost "test"
    }

    @Test
    fun `Skal ikke feile ved valgfri miljøvariabel`() {
        shouldNotThrowAny {
            val FOOBAR by EnvironmentVariable<String?>(transform = ::identity)
            FOOBAR.shouldBeNull()
        }
    }
}
