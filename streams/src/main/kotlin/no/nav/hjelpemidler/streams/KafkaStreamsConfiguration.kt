package no.nav.hjelpemidler.streams

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import no.nav.hjelpemidler.configuration.KafkaEnvironmentVariable
import no.nav.hjelpemidler.kafka.createKafkaClientConfiguration
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.state.BuiltInDslStoreSuppliers.InMemoryDslStoreSuppliers
import java.util.Properties

internal fun kafkaSchemaRegistryConfiguration(): Map<String, String> = mapOf(
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to KafkaEnvironmentVariable.KAFKA_SCHEMA_REGISTRY,
    AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE to "USER_INFO",
    SchemaRegistryClientConfig.USER_INFO_CONFIG to "${KafkaEnvironmentVariable.KAFKA_SCHEMA_REGISTRY_USER}:${KafkaEnvironmentVariable.KAFKA_SCHEMA_REGISTRY_PASSWORD}",
)

internal fun kafkaStreamsConfiguration(applicationId: String): Properties = createKafkaClientConfiguration {
    this[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
    this[StreamsConfig.DSL_STORE_SUPPLIERS_CLASS_CONFIG] = InMemoryDslStoreSuppliers::class.java.name
}
