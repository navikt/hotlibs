package no.nav.hjelpemidler.database.hibernate

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import org.hibernate.type.descriptor.WrapperOptions
import kotlin.test.Test

class EnhetsnummerJavaTypeTest {
    private val enhetsnummer = Enhetsnummer("2990")
    private val options: WrapperOptions = mockk()

    @Test
    fun unwrap() {
        assertSoftly(EnhetsnummerJavaType.INSTANCE) {
            unwrap(null, String::class, options) shouldBe null
            unwrap(enhetsnummer, Enhetsnummer::class, options) shouldBeSameInstanceAs enhetsnummer
            unwrap(enhetsnummer, String::class, options) shouldBe enhetsnummer.value
        }
    }

    @Test
    fun wrap() {
        assertSoftly(EnhetsnummerJavaType.INSTANCE) {
            wrap(null, options) shouldBe null
            wrap(enhetsnummer, options) shouldBeSameInstanceAs enhetsnummer
            wrap(enhetsnummer.value, options) shouldBe enhetsnummer
            wrap(enhetsnummer.value.toInt(), options) shouldBe enhetsnummer
            wrap(enhetsnummer.value.toLong(), options) shouldBe enhetsnummer
        }
    }
}
