package no.nav.hjelpemidler.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private val log = KotlinLogging.logger {}

/**
 * Bruker [suspendCancellableCoroutine] for å gjøre om [Producer.send] med [Callback] til en suspended function.
 */
suspend fun <K : Any, V : Any> Producer<K, V>.sendSuspending(
    record: ProducerRecord<K, V>,
): RecordMetadata = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            log.trace { "sendSuspending cancelled, but the message may still be delivered" }
        }
        send(record) { metadata: RecordMetadata, exception: Exception? ->
            if (exception == null) {
                continuation.resume(metadata)
            } else {
                continuation.resumeWithException(exception)
            }
        }
    }
}
