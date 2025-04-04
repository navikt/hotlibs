package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.test_support.TestRapid
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageProblems
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.micrometer.core.instrument.MeterRegistry
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import java.util.UUID
import kotlin.test.Test

class KafkaMessageListenerTest {
    private val listener = RecordingKafkaMessageListener()

    private val søknadId = UUID.randomUUID()
    private val fnrBruker = Fødselsnummer(50.år)

    @Test
    fun `onMessage() blir kalt med TestMessage`() {
        sendTestMessage(
            "id" to "1",
            "vedtakId" to "2",
            "søknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeEmpty()
        listener.messages.shouldBeSingleton {
            it.id shouldBe "1"
            it.vedtakId shouldBe "2"
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe fnrBruker.toString()
        }
    }


    @Test
    fun `onMessage() blir kalt med TestMessage, alternativt eventName`() {
        sendTestMessage(
            "id" to "1",
            "vedtakId" to "2",
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.ALTERNATIVE_EVENT_NAME,
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeEmpty()
        listener.messages.shouldBeSingleton {
            it.id shouldBe "1"
            it.vedtakId shouldBe "2"
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe fnrBruker.toString()
        }
    }

    @Test
    fun `onMessage() blir kalt med TestMessage, mangler vedtakId som er optional`() {
        sendTestMessage(
            "id" to "1",
            "vedtakId" to null,
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeEmpty()
        listener.messages.shouldBeSingleton {
            it.id shouldBe "1"
            it.vedtakId shouldBe null
            it.søknadId shouldBe søknadId
            it.brukerFnr shouldBe fnrBruker.toString()
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
        listener.messages.shouldBeEmpty()
    }

    @Test
    fun `onError() blir kalt, mangler id`() {
        sendTestMessage(
            "id" to null,
            "vedtakId" to null,
            "soknadId" to søknadId,
            "fnrBruker" to fnrBruker,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        listener.preconditionErrors.shouldBeEmpty()
        listener.errors.shouldBeSingleton {
            it.hasErrors() shouldBe true
        }
        listener.messages.shouldBeEmpty()
    }

    private fun sendTestMessage(vararg pairs: Pair<String, Any?>) {
        val connection = TestRapid()
        connection.register<TestMessage>(listener)
        connection.sendTestMessage(valueToJson(mapOf(*pairs)))
    }
}

private class RecordingKafkaMessageListener : KafkaMessageListener<TestMessage>(failOnError = false) {
    override fun onPreconditionError(error: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        preconditionErrors.add(error)
    }

    override fun onError(problems: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        super.onError(problems, context, metadata)
        errors.add(problems)
    }

    override fun skipMessage(
        message: JsonMessage,
        context: ExtendedMessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    ): Boolean = false

    override suspend fun onMessage(
        message: TestMessage,
        context: ExtendedMessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    ) {
        messages.add(message)
    }

    val preconditionErrors = mutableListOf<MessageProblems>()
    val errors = mutableListOf<MessageProblems>()
    val messages = mutableListOf<TestMessage>()
}
