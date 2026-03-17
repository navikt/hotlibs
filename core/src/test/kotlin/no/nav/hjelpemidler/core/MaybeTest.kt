package no.nav.hjelpemidler.core

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.core.Maybe.Absent
import kotlin.test.Test

class MaybeTest {
    @Test
    fun `Absent isAbsent should be true`() {
        assertSoftly(Absent) {
            isAbsent() shouldBe true
            isPresent() shouldBe false
        }
    }

    @Test
    fun `Present isPresent should be true`() {
        assertSoftly(Maybe("test")) {
            isAbsent() shouldBe false
            isPresent() shouldBe true
        }
    }
}
