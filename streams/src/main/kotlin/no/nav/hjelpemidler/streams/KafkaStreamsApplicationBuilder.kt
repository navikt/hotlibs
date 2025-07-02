package no.nav.hjelpemidler.streams

import io.ktor.server.application.Application
import org.apache.kafka.streams.StreamsBuilder

class KafkaStreamsApplicationBuilder internal constructor() {
    internal val streamsBuilder: StreamsBuilder = StreamsBuilder()
    internal var applicationBlock: (suspend Application.() -> Unit)? = null

    fun streams(block: StreamsBuilder.() -> Unit) {
        streamsBuilder.apply(block)
    }

    fun application(block: suspend Application.() -> Unit) {
        applicationBlock = block
    }
}
