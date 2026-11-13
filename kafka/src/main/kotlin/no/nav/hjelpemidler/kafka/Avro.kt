package no.nav.hjelpemidler.kafka

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import no.nav.hjelpemidler.configuration.ClusterEnvironment
import no.nav.hjelpemidler.configuration.Environment
import no.nav.hjelpemidler.configuration.KafkaEnvironmentVariable

fun kafkaSchemaRegistryConfiguration(): Map<String, String> = mapOf(
    AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS to if (Environment.current is ClusterEnvironment) "false" else "true",
    AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION to "true",
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to KafkaEnvironmentVariable.KAFKA_SCHEMA_REGISTRY,
    AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE to "USER_INFO",
    SchemaRegistryClientConfig.USER_INFO_CONFIG to KafkaEnvironmentVariable.USER_INFO_CONFIG,
)
