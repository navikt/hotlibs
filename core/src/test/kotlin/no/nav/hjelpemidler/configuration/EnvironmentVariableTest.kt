package no.nav.hjelpemidler.configuration

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.uri.shouldHaveHost
import io.kotest.matchers.url.shouldHaveHost
import no.nav.hjelpemidler.text.toUUID
import java.net.URI
import java.net.URL
import java.util.UUID
import kotlin.test.Test

class EnvironmentVariableTest {
    @Test
    fun `Skal kunne lese miljøvariabel som Boolean`() {
        val ENABLED: Boolean by EnvironmentVariable()
        ENABLED.shouldBeTrue()

        val ACTIVE: Boolean? by EnvironmentVariable()
        ACTIVE.shouldBeNull()

        val DEVELOPMENT: Boolean by EnvironmentVariable(defaultValue = true)
        DEVELOPMENT.shouldBeTrue()
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Int`() {
        val PORT: Int by EnvironmentVariable()
        PORT shouldBe 8080

        val MAXIMUM_POOL_SIZE: Int? by EnvironmentVariable()
        MAXIMUM_POOL_SIZE.shouldBeNull()

        val TIMEOUT: Int by EnvironmentVariable(defaultValue = 3600)
        TIMEOUT shouldBe 3600
    }

    @Test
    fun `Skal kunne lese miljøvariabel som Double`() {
        val RATE: Double by EnvironmentVariable()
        RATE shouldBe 3.35

        val WEIGHT: Double? by EnvironmentVariable()
        WEIGHT.shouldBeNull()

        val ANGLE: Double by EnvironmentVariable(defaultValue = 45.0)
        ANGLE shouldBe 45.0
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URI`() {
        val API_URI: URI by EnvironmentVariable()
        API_URI shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som URL`() {
        val API_URL: URL by EnvironmentVariable()
        API_URL shouldHaveHost "test"
    }

    @Test
    fun `Skal kunne lese miljøvariabel som UUID`() {
        val TEST_ID: UUID by EnvironmentVariable()
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
