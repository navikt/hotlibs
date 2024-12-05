package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.kafka.Config
import no.nav.hjelpemidler.configuration.KafkaEnvironmentVariable
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.security.auth.SecurityProtocol
import java.util.Properties

class LocalConfig(private val brokers: List<String>) : Config {
    constructor(broker: String) : this(broker.split(',').map(String::trim))

    companion object {
        val default get() = LocalConfig(KafkaEnvironmentVariable.KAFKA_BROKERS)
    }

    init {
        require(brokers.isNotEmpty())
    }

    override fun producerConfig(properties: Properties) = Properties().apply {
        putAll(kafkaBaseConfig())
        put(ProducerConfig.ACKS_CONFIG, "1")
        put(ProducerConfig.LINGER_MS_CONFIG, "0")
        put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1")
        putAll(properties)
    }

    override fun consumerConfig(groupId: String, properties: Properties) = Properties().apply {
        putAll(kafkaBaseConfig())
        put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        putAll(properties)
        put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    }

    override fun adminConfig(properties: Properties) = Properties().apply {
        putAll(kafkaBaseConfig())
        putAll(properties)
    }

    private fun kafkaBaseConfig() = Properties().apply {
        put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers)
        put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.PLAINTEXT.name)
    }
}
