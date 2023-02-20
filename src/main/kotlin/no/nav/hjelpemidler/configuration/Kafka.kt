package no.nav.hjelpemidler.configuration

object KafkaEnvironmentVariable {
    val KAFKA_BROKERS by EnvironmentVariable
    val KAFKA_CA by EnvironmentVariable
    val KAFKA_CA_PATH by EnvironmentVariable
    val KAFKA_CERTIFICATE by EnvironmentVariable
    val KAFKA_CERTIFICATE_PATH by EnvironmentVariable
    val KAFKA_CREDSTORE_PASSWORD by EnvironmentVariable
    val KAFKA_KEYSTORE_PATH by EnvironmentVariable
    val KAFKA_PRIVATE_KEY by EnvironmentVariable
    val KAFKA_PRIVATE_KEY_PATH by EnvironmentVariable
    val KAFKA_SCHEMA_REGISTRY by EnvironmentVariable
    val KAFKA_SCHEMA_REGISTRY_PASSWORD by EnvironmentVariable
    val KAFKA_SCHEMA_REGISTRY_USER by EnvironmentVariable
    val KAFKA_TRUSTSTORE_PATH by EnvironmentVariable
}
