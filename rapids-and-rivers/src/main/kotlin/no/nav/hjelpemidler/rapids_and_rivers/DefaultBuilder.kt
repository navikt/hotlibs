package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.kafka.ConsumerProducerFactory
import com.github.navikt.tbd_libs.rapids_and_rivers.createDefaultKafkaRapidFromEnv
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.prometheus.metrics.model.registry.PrometheusRegistry
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.hjelpemidler.configuration.Configuration
import java.net.InetAddress
import java.util.UUID

fun RapidApplication.DefaultBuilder(
    env: Map<String, String> = Configuration.current,
    consumerProducerFactory: ConsumerProducerFactory = ConsumerProducerFactory(AutoConfig()),
): RapidApplication.Builder {
    val meterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, PrometheusRegistry.defaultRegistry, Clock.SYSTEM)

    val kafkaRapid = createDefaultKafkaRapidFromEnv(
        factory = consumerProducerFactory,
        meterRegistry = meterRegistry,
        env = env,
    )

    return RapidApplication.Builder(
        appName = Configuration.current["RAPID_APP_NAME"],
        instanceId = when (env.containsKey("NAIS_APP_NAME")) {
            true -> InetAddress.getLocalHost().hostName
            false -> UUID.randomUUID().toString()
        },
        rapid = kafkaRapid,
        meterRegistry = meterRegistry,
    )
}