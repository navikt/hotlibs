package no.nav.hjelpemidler.streams

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.apache.kafka.streams.KafkaStreams

internal fun Route.health(kafkaStreams: KafkaStreams) {
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
}
