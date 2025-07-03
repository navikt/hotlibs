package no.nav.hjelpemidler.streams

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.apache.kafka.streams.KafkaStreams

internal fun Route.health(meterRegistry: MeterRegistry, kafkaStreams: KafkaStreams) {
    get("/isalive") {
        val state = kafkaStreams.state()
        if (state == KafkaStreams.State.RUNNING || state == KafkaStreams.State.REBALANCING) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.ServiceUnavailable, "KafkaStreams state: $state")
        }
    }

    get("/isready") {
        val state = kafkaStreams.state()
        if (state == KafkaStreams.State.RUNNING) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.ServiceUnavailable, "KafkaStreams state: $state")
        }
    }

    if (meterRegistry is PrometheusMeterRegistry) {
        get("/metrics") {
            call.respond(meterRegistry.scrape())
        }
    }
}
