package no.nav.hjelpemidler.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val log = KotlinLogging.logger {}

/**
 * Bruker [suspendCoroutine] for å gjøre om send med [Callback] til en suspended function.
 */
suspend fun <K : Any, V : Any> Producer<K, V>.sendAsync(
    record: ProducerRecord<K, V>,
): RecordMetadata = suspendCancellableCoroutine { continuation ->
    continuation.invokeOnCancellation {
        log.trace { "sendAsync cancelled, but message may still be delivered" }
    }
    val callback = Callback { metadata: RecordMetadata, exception: Exception? ->
        if (exception == null) {
            continuation.resume(metadata)
        } else {
            continuation.resumeWithException(exception)
        }
    }
    send(record, callback)
}
