package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.test_support.TestRapid
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageProblems
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import java.util.UUID
import kotlin.test.Test

class EventListenerTest {
    private val rapid = TestRapid()
    private val listener = rapid.let(::TestEventListener)

    private val søknadId = UUID.randomUUID()
    private val fnrBruker = Fødselsnummer(50.år)

    @Test
    fun `onEvent() blir kalt med TestEvent`() {
        sendTestMessage(
            "id" to "1",
            "vedtakId" to "2",
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to "hm-test-event",
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeEmpty()
        listener.events.shouldBeSingleton {
            it.id shouldBe "1"
            it.vedtakId shouldBe "2"
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe fnrBruker.toString()
            it.eventName shouldBe "hm-test-event"
        }
    }

    @Test
    fun `onEvent() blir kalt med TestEvent, mangler vedtakId`() {
        sendTestMessage(
            "id" to "1",
            "vedtakId" to null,
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to "hm-test-event",
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeEmpty()
        listener.events.shouldBeSingleton {
            it.id shouldBe "1"
            it.vedtakId shouldBe null
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe fnrBruker.toString()
            it.eventName shouldBe "hm-test-event"
        }
    }

    @Test
    fun `onPreconditionError() blir kalt, ukjent eventName`() {
        sendTestMessage(
            "eventId" to UUID.randomUUID(),
            "eventName" to "hm-some-other-event",
        )

        listener.preconditionErrors.shouldBeSingleton {
            it.hasErrors() shouldBe true
        }
        listener.errors.shouldBeEmpty()
        listener.events.shouldBeEmpty()
    }

    @Test
    fun `onError() blir kalt, mangler id`() {
        sendTestMessage(
            "id" to null,
            "vedtakId" to null,
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to "hm-test-event",
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeSingleton {
            it.hasErrors() shouldBe true
        }
        listener.events.shouldBeEmpty()
    }

    private fun sendTestMessage(vararg pairs: Pair<String, Any?>) =
        rapid.sendTestMessage(valueToJson(mapOf(*pairs)))
}

private class TestEventListener(connection: RapidsConnection) : EventListener<TestEvent>(
    eventClass = TestEvent::class,
    failOnError = false,
) {
    init {
        connection.register<TestEvent>(this)
    }

    override fun skipEvent(packet: JsonMessage, context: MessageContext): Boolean = false

    override fun onPreconditionError(error: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        preconditionErrors.add(error)
    }

    override fun onError(problems: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        super.onError(problems, context, metadata)
        errors.add(problems)
    }

    override suspend fun onEvent(event: TestEvent, context: MessageContext) {
        events.add(event)
    }

    val preconditionErrors = mutableListOf<MessageProblems>()
    val errors = mutableListOf<MessageProblems>()
    val events = mutableListOf<TestEvent>()
}
