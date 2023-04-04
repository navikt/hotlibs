package no.nav.hjelpemidler.configuration

object KafkaEnvironmentVariable {
    @External
    val KAFKA_BROKERS by EnvironmentVariable

    @External
    val KAFKA_CA by EnvironmentVariable

    @External
    val KAFKA_CA_PATH by EnvironmentVariable

    @External
    val KAFKA_CERTIFICATE by EnvironmentVariable

    @External
    val KAFKA_CERTIFICATE_PATH by EnvironmentVariable

    @External
    val KAFKA_CREDSTORE_PASSWORD by EnvironmentVariable

    @External
    val KAFKA_KEYSTORE_PATH by EnvironmentVariable

    @External
    val KAFKA_PRIVATE_KEY by EnvironmentVariable

    @External
    val KAFKA_PRIVATE_KEY_PATH by EnvironmentVariable

    @External
    val KAFKA_SCHEMA_REGISTRY by EnvironmentVariable

    @External
    val KAFKA_SCHEMA_REGISTRY_PASSWORD by EnvironmentVariable

    @External
    val KAFKA_SCHEMA_REGISTRY_USER by EnvironmentVariable

    @External
    val KAFKA_TRUSTSTORE_PATH by EnvironmentVariable
}
