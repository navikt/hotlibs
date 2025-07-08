package no.nav.hjelpemidler.configuration

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uri.shouldHaveHost
import io.kotest.matchers.url.shouldHaveHost
import no.nav.hjelpemidler.text.toUUID
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `Skal kunne lese miljøvariabel som Boolean`() {
        val ENABLED by EnvironmentVariable.boolean()
        ENABLED.shouldBeTrue()

        val ACTIVE: Boolean? by EnvironmentVariable.boolean()
        ACTIVE.shouldBeNull()

        val DEVELOPMENT by EnvironmentVariable.boolean(defaultValue = true)
        DEVELOPMENT.shouldBeTrue()
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Int`() {
        val PORT by EnvironmentVariable.int()
        PORT shouldBe 8080

        val MAXIMUM_POOL_SIZE: Int? by EnvironmentVariable.int()
        MAXIMUM_POOL_SIZE.shouldBeNull()

        val TIMEOUT by EnvironmentVariable.int(defaultValue = 3600)
        TIMEOUT shouldBe 3600
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Double`() {
        val RATE by EnvironmentVariable.double()
        RATE shouldBe 3.35

        val WEIGHT: Double? by EnvironmentVariable.double()
        WEIGHT.shouldBeNull()

        val ANGLE by EnvironmentVariable.double(defaultValue = 45.0)
        ANGLE shouldBe 45.0
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URI`() {
        val API_URI by EnvironmentVariable.uri()
        API_URI shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URL`() {
        val API_URL by EnvironmentVariable.url()
        API_URL shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som UUID`() {
        val TEST_ID by EnvironmentVariable.uuid()
        TEST_ID shouldBe "2fe28df1-8549-4d5a-a37d-a7a044927e10".toUUID()
    }

    @Test
    fun `Skal ikke feile ved valgfri miljøvariabel`() {
        shouldNotThrowAny {
            val FOOBAR: String? by EnvironmentVariable
            FOOBAR.shouldBeNull()
        }
    }
}
