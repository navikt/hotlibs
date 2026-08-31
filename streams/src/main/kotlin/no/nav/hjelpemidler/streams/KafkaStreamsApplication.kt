package no.nav.hjelpemidler.streams

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder

fun kafkaStreamsApplication(
    applicationId: String,
    port: Int = 8080,
    block: KafkaStreamsApplicationBuilder.() -> Unit,
): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    val builder = KafkaStreamsApplicationBuilder().apply(block)
    return embeddedServer(factory = Netty, port = port) {
        main(applicationId, builder)
        builder.applicationBlock?.invoke(this)
    }
}

internal fun Application.main(applicationId: String, builder: KafkaStreamsApplicationBuilder) {
    kafkaStreams(applicationId, builder.streamsBuilder)
    health()
    metrics()
}

internal fun Application.kafkaStreams(applicationId: String, builder: StreamsBuilder) {
    dependencies {
        provide<KafkaStreams> {
            KafkaStreams(builder.build(), kafkaStreamsConfiguration(applicationId))
        }
        provide<KafkaStreamsMetrics> {
            KafkaStreamsMetrics(resolve<KafkaStreams>())
        }
    }

    install(KafkaStreamsPlugin)
}

internal fun Application.metrics() {
    dependencies {
        provide<MeterRegistry> {
            PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        }
    }

    val meterRegistry: MeterRegistry by dependencies
    install(MicrometerMetrics) {
        registry = meterRegistry
    }

    val registry = meterRegistry
    if (registry is PrometheusMeterRegistry) {
        routing {
            get("/metrics") {
                call.respond(registry.scrape())
            }
        }
    }
}
