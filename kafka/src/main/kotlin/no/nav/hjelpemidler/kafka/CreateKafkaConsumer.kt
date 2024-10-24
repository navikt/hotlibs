package no.nav.hjelpemidler.kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.Properties

/**
 * Oppretter en standard [Consumer] for [no.nav.hjelpemidler.configuration.Environment.current].
 *
 * @see [createKafkaClientConfiguration]
 * @see [createMockConsumer]
 */
fun createKafkaConsumer(configure: Properties.() -> Unit = {}): Consumer<String, String> {
    val deserializer = StringDeserializer()
    return KafkaConsumer(
        createKafkaClientConfiguration(configure),
        deserializer,
        deserializer,
    )
}

fun createMockConsumer(offsetResetStrategy: OffsetResetStrategy = OffsetResetStrategy.NONE): MockConsumer<String, String> {
    return MockConsumer<String, String>(offsetResetStrategy)
}
