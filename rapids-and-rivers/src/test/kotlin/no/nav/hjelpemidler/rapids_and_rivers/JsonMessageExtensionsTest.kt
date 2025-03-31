package no.nav.hjelpemidler.rapids_and_rivers

import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.toJson
import java.util.UUID
import kotlin.test.Test

class JsonMessageExtensionsTest {
    @Test
    fun `JsonMessage skal valideres basert på TestKafkaEvent`() {
        val søknadId = UUID.randomUUID()
        val brukerFnr = Fødselsnummer(75.år)
        val message = jsonMessageOf(
            "id" to "1",
            "soknadId" to søknadId,
            "fnrBruker" to brukerFnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestKafkaEvent.EVENT_NAME,
        )

        message.require<TestKafkaEvent>()

        val event = message.value<TestKafkaEvent>()
        event.should {
            it.id shouldBe "1"
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe brukerFnr.toString()
            it.eventName shouldBe TestKafkaEvent.EVENT_NAME
        }

        event.toJson().should {
            it.shouldContainJsonKeyValue("eventName", TestKafkaEvent.EVENT_NAME)
        }
    }
}
