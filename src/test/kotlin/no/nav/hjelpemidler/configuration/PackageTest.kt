package no.nav.hjelpemidler.configuration

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import java.time.Instant
import kotlin.test.Test

class PackageTest {
    @Test
    fun `henter alle variabler, inkludert eksterne`() {
        val variables = environmentVariablesIn(TestConfiguration, includeExternal = true)
        variables shouldContainExactlyInAnyOrder listOf("FOO", "BAR")
    }

    @Test
    fun `henter variabler som ikke er markert som eksterne`() {
        val variables = environmentVariablesIn(TestConfiguration, includeExternal = false)
        variables shouldContainExactlyInAnyOrder listOf("BAR")
    }

    @Suppress("unused")
    private object TestConfiguration {
        @External
        val FOO by EnvironmentVariable
        val BAR by EnvironmentVariable
        const val test = "test"
        val instant: Instant = Instant.now()
    }
}
