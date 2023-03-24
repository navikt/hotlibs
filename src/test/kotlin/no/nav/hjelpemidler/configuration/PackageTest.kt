package no.nav.hjelpemidler.configuration

import kotlin.test.Test

class PackageTest {
    @Test
    fun `foo bar`() {
        TODO("Not yet implemented")
    }

    private object TestConfiguration {
        @External
        val FOO by EnvironmentVariable
        val BAR by EnvironmentVariable
    }
}
