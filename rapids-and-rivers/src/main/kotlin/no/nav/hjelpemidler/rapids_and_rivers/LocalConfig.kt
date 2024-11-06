package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.kafka.Config
import no.nav.hjelpemidler.collections.propertiesOf
import no.nav.hjelpemidler.configuration.Configuration
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.security.auth.SecurityProtocol
import java.util.*

class LocalConfig(
    private val brokers: List<String>,
) : Config {
    companion object {
        val default get() = LocalConfig(
            brokers = requireNotNull(Configuration.current["KAFKA_BROKERS"]) { "Expected KAFKA_BROKERS" }.split(',').map(String::trim),
        )
    }

    init {
        check(brokers.isNotEmpty())
    }

    override fun producerConfig(properties: Properties) = properties.apply {
        putAll(mapOf(
            ProducerConfig.ACKS_CONFIG to "1",
        ))
    }

        /*Properties().apply {
        putAll(kafkaBaseConfig())
        put(ProducerConfig.ACKS_CONFIG, "1")
        put(ProducerConfig.LINGER_MS_CONFIG, "0")
        put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1")
        putAll(properties)
    }*/

    override fun consumerConfig(groupId: String, properties:  Properties) = Properties().apply {
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
