package no.nav.hjelpemidler.streams

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import java.util.Properties

fun topology(block: StreamsBuilder.() -> Unit): Topology = StreamsBuilder().apply(block).build()

fun kafkaStreams(
    applicationId: String,
    configuration: Properties = kafkaStreamsConfiguration(applicationId),
    block: StreamsBuilder.() -> Unit,
): KafkaStreams = KafkaStreams(topology(block), configuration)

infix fun <K, V> K.withValue(value: V): KeyValue<K, V> = KeyValue.pair(this, value)

inline fun <K : Any, reified V : Any> KStream<K, V>.toRapid(
    keySerde: Serde<K>,
    topic: String = "teamdigihot.hm-soknadsbehandling-v1",
) {
    to(topic, Produced.with(keySerde, jsonSerde<V>()))
}
