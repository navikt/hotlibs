package no.nav.hjelpemidler.collections

import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import kotlin.test.Test

class MapsTest {
    @Test
    fun `Map med kun null-verdi er tomt`() {
        mapOfNotNull("test" to null).shouldBeEmpty()
    }

    @Test
    fun `Map med flere null-verdier er tomt`() {
        mapOfNotNull("test1" to null, "test2" to null).shouldBeEmpty()
    }

    @Test
    fun `Map med null-verdi og verdi er ikke tomt`() {
        val entry = "test2" to "test2"
        mapOfNotNull("test1" to null, entry).shouldContain(entry)
    }
}
