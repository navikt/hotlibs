package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class EksternIdTest {
    @Test
    fun `EksternId skal ha riktige verdier`() {
        eksternId.application shouldBe "hotsak"
        eksternId.resource shouldBe "/saksnotat"
        eksternId.parameters.should { parameters ->
            parameters.shouldContain("sakId", listOf("1"))
            parameters.shouldContain("saksnotatId", listOf("2"))
            parameters.shouldContain("brevsendingId", emptyList())
        }

        eksternId["sakId"] shouldBe "1"
        eksternId["saksnotatId"] shouldBe "2"

        eksternId.toURI().scheme shouldBe EksternId.SCHEME
    }
}
