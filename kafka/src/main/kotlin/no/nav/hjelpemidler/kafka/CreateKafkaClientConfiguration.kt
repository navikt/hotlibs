package no.nav.hjelpemidler.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.collections.propertiesOf
import no.nav.hjelpemidler.configuration.Environment
import no.nav.hjelpemidler.configuration.KafkaEnvironmentVariable
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.security.auth.SecurityProtocol
import java.util.Properties

private val log = KotlinLogging.logger {}

/**
 * Oppretter standard Kafka-konfigurasjon for [no.nav.hjelpemidler.configuration.Environment.current].
 *
 * @see [createKafkaConsumer]
 * @see [createKafkaProducer]
 */
fun createKafkaClientConfiguration(configure: Properties.() -> Unit = {}): Properties {
    log.info { "Oppretter Kafka-konfigurasjon for: ${Environment.current}" }
    return when (Environment.current.tier) {
        Environment.Tier.TEST, Environment.Tier.LOCAL -> propertiesOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to KafkaEnvironmentVariable.KAFKA_BROKERS,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to SecurityProtocol.PLAINTEXT.name,

            SaslConfigs.SASL_MECHANISM to "PLAIN",
        )

        else -> propertiesOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to KafkaEnvironmentVariable.KAFKA_BROKERS,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to SecurityProtocol.SSL.name,

            SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG to "",
            SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG to KafkaEnvironmentVariable.KAFKA_KEYSTORE_PATH,
            SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG to KafkaEnvironmentVariable.KAFKA_CREDSTORE_PASSWORD,
            SslConfigs.SSL_KEYSTORE_TYPE_CONFIG to "PKCS12",
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG to KafkaEnvironmentVariable.KAFKA_TRUSTSTORE_PATH,
            SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG to KafkaEnvironmentVariable.KAFKA_CREDSTORE_PASSWORD,
            SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG to "JKS",
        )
    }.apply {
        this[ProducerConfig.ACKS_CONFIG] = "all"
        this[ProducerConfig.LINGER_MS_CONFIG] = "0"
        this[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] = "1"
    }.apply(configure)
}
