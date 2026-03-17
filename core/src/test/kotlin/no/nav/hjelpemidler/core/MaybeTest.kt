package no.nav.hjelpemidler.core

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.core.Maybe.Absent
import kotlin.test.Test

class MaybeTest {
    @Test
    fun isAbsent() {
        val absent: Maybe<String> = Absent
        assertSoftly(absent) {
            isAbsent() shouldBe true
            isPresent() shouldBe false
        }
    }

    @Test
    fun isPresent() {
        val present: Maybe<String> = Maybe("test")
        assertSoftly(present) {
            isAbsent() shouldBe false
            isPresent() shouldBe true
        }
    }

    @Test
    fun filter() {
        val present = Maybe("permitted")
        assertSoftly(present) {
            filter { it == "permitted" } shouldBe present
            filter { it == "forbidden" } shouldBe Absent
        }
    }

    @Test
    fun filterNot() {
        val present = Maybe("forbidden")
        assertSoftly(present) {
            filterNot { it == "forbidden" } shouldBe Absent
            filterNot { it == "permitted" } shouldBe present
        }
    }
}
