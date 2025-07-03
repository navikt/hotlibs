package no.nav.hjelpemidler.streams

import no.nav.hjelpemidler.configuration.Configuration
import no.nav.hjelpemidler.streams.serialization.jsonSerde
import no.nav.hjelpemidler.streams.serialization.serde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced

infix fun <K, V> K.withValue(value: V): KeyValue<K, V> =
    KeyValue.pair(this, value)

inline fun <reified K, reified V : Any> KStream<K, V>.toRapid(
    keySerde: Serde<K> = serde<K>(),
    topic: String = Configuration.current["KAFKA_RAPID_TOPIC"] ?: "teamdigihot.hm-soknadsbehandling-v1",
) {
    to(topic, Produced.with(keySerde, jsonSerde<V>()))
}
