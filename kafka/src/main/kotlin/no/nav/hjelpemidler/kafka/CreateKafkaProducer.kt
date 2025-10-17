package no.nav.hjelpemidler.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

/**
 * Oppretter en standard [Producer] for [no.nav.hjelpemidler.configuration.Environment.current].
 *
 * @see [createKafkaClientConfiguration]
 * @see [createMockProducer]
 */
fun createKafkaProducer(configure: Properties.() -> Unit = {}): Producer<String, String> {
    val serializer = StringSerializer()
    return KafkaProducer(
        createKafkaClientConfiguration(configure),
        serializer,
        serializer,
    )
}

fun createMockProducer(autoComplete: Boolean = true): MockProducer<String, String> {
    val serializer = StringSerializer()
    return MockProducer(autoComplete, null, serializer, serializer)
}
