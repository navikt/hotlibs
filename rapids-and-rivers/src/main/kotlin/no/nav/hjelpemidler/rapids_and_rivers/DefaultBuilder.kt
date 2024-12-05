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

fun RapidApplication.Companion.DefaultBuilder(
    env: Map<String, String> = Configuration.current,
    consumerProducerFactory: ConsumerProducerFactory = ConsumerProducerFactory(autoConfig()),
    meterRegistry: PrometheusMeterRegistry = PrometheusMeterRegistry(
        PrometheusConfig.DEFAULT,
        PrometheusRegistry.defaultRegistry,
        Clock.SYSTEM,
    ),
): RapidApplication.Builder {
    val kafkaRapid = createDefaultKafkaRapidFromEnv(
        factory = consumerProducerFactory,
        meterRegistry = meterRegistry,
        env = env,
    )

    return RapidApplication.Builder(
        appName = Configuration.current["RAPID_APP_NAME"],
        instanceId = if ("NAIS_APP_NAME" in env) InetAddress.getLocalHost().hostName else UUID.randomUUID().toString(),
        rapid = kafkaRapid,
        meterRegistry = meterRegistry,
    )
}
