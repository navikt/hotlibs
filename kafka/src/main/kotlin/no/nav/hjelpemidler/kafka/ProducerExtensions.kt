package no.nav.hjelpemidler.kafka

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Bruker [suspendCoroutine] for å gjøre om send med [Callback] til en suspended function.
 */
suspend inline fun <reified K : Any, reified V : Any> Producer<K, V>.sendAsync(
    record: ProducerRecord<K, V>,
): RecordMetadata = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine { continuation ->
        val callback = Callback { metadata, exception ->
            if (exception == null) {
                continuation.resume(metadata)
            } else {
                continuation.resumeWithException(exception)
            }
        }
        send(record, callback)
    }
}
