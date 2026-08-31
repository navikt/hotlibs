package no.nav.hjelpemidler.streams

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.apache.kafka.streams.KafkaStreams

internal fun Application.health() {
    val kafkaStreams: KafkaStreams by dependencies

    routing {
        get("/isalive") {
            val state = kafkaStreams.state()
            if (state.isRunningOrRebalancing) {
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
}
