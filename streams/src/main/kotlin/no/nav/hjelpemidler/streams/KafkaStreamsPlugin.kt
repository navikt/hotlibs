package no.nav.hjelpemidler.streams

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.events.EventDefinition
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.plugins.di.dependencies
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KafkaStreams.State

private val log = KotlinLogging.logger {}

internal val KafkaStreamsPlugin = createApplicationPlugin("KafkaStreamsPlugin") {
    val kafkaStreams: KafkaStreams by application.dependencies

    kafkaStreams.setStateListener { newState, oldState ->
        application.monitor.raise(
            KafkaStreamsStateTransitionEvent,
            KafkaStreamsStateTransition(newState, oldState),
        )
    }

    val kafkaStreamsMetrics: KafkaStreamsMetrics by application.dependencies
    val meterRegistry: MeterRegistry by application.dependencies
    kafkaStreamsMetrics.bindTo(meterRegistry)

    on(MonitoringEvent(ApplicationStarted)) {
        kafkaStreams.cleanUp()
        kafkaStreams.start()
        log.info { "Kafka Streams startet" }
    }
}

internal data class KafkaStreamsStateTransition(val newState: State, val oldState: State)

internal val KafkaStreamsStateTransitionEvent: EventDefinition<KafkaStreamsStateTransition> = EventDefinition()
