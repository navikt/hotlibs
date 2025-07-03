package no.nav.hjelpemidler.streams

import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun kafkaStreamsApplication(
    applicationId: String,
    port: Int = 8080,
    meterRegistry: MeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT),
    block: KafkaStreamsApplicationBuilder.() -> Unit,
): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    val builder = KafkaStreamsApplicationBuilder().apply(block)
    return embeddedServer(factory = Netty, port = port) {
        install(MicrometerMetrics) {
            registry = meterRegistry
        }

        val kafkaStreams = kafkaStreams(applicationId, builder = builder.streamsBuilder)
        install(KafkaStreamsPlugin) {
            this.kafkaStreams = kafkaStreams
        }

        routing {
            health(meterRegistry, kafkaStreams)
        }

        builder.applicationBlock?.invoke(this)
    }
}
