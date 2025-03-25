package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class URNTest {
    @Test
    fun `URN skal ha riktig NID og NSS`() {
        assertSoftly(URN("hotsak", "saksnotat", "1")) {
            it.nid shouldBe "hotsak"
            it.nss shouldBe "saksnotat:1"
            it.toURI().scheme shouldBe URN.SCHEME
        }
    }
}
