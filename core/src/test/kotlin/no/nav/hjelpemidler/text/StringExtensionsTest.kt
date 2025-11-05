package no.nav.hjelpemidler.text

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class StringExtensionsTest {
    @Test
    fun quoted() {
        "".quoted() shouldBe "''"
        "test".quoted() shouldBe "'test'"
        "'test'".quoted() shouldBe "'test'"
        "test".quoted().unquoted() shouldBe "test"
    }

    @Test
    fun doubleQuoted() {
        "".doubleQuoted() shouldBe "\"\""
        "test".doubleQuoted() shouldBe "\"test\""
        "\"test\"".doubleQuoted() shouldBe "\"test\""
        "test".doubleQuoted().unquoted() shouldBe "test"
    }
}
