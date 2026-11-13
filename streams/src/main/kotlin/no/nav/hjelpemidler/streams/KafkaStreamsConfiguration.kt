package no.nav.hjelpemidler.streams

import no.nav.hjelpemidler.kafka.createKafkaClientConfiguration
import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.state.BuiltInDslStoreSuppliers.InMemoryDslStoreSuppliers
import java.util.Properties

internal fun kafkaStreamsConfiguration(applicationId: String): Properties = createKafkaClientConfiguration {
    this[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
    this[StreamsConfig.DSL_STORE_SUPPLIERS_CLASS_CONFIG] = InMemoryDslStoreSuppliers::class.java.name
    this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = StringSerde::class.java.name
    this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = StringSerde::class.java.name
}
