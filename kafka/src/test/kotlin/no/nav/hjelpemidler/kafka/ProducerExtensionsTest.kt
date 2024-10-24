package no.nav.hjelpemidler.kafka

import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.matchers.collections.shouldContainOnly
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.apache.kafka.clients.producer.ProducerRecord
import kotlin.test.Test

class ProducerExtensionsTest {
    private val producer = createMockProducer(autoComplete = false)
    private val record = ProducerRecord("topic", "key", "value")

    @Test
    fun `Sender melding uten feil`() = runTest {
        launch { producer.sendAsync(record) }

        launch {
            while (!producer.completeNext()) {
                // venter til melding er sendt
            }
        }

        testScheduler.advanceUntilIdle()

        producer.history().shouldContainOnly(record)
    }

    @Test
    fun `Sender melding med feil`() = runTest {
        val message = "Noe gikk galt!"

        launch { shouldThrowWithMessage<RuntimeException>(message) { producer.sendAsync(record) } }

        launch {
            while (!producer.errorNext(RuntimeException(message))) {
                // venter til melding er sendt
            }
        }

        testScheduler.advanceUntilIdle()

        producer.history().shouldContainOnly(record)
    }
}
