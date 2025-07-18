package no.nav.hjelpemidler.streams

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.events.EventDefinition
import io.ktor.events.EventHandler
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KafkaStreams.State

private val log = KotlinLogging.logger {}

internal class KafkaStreamsPluginConfiguration {
    lateinit var kafkaStreams: KafkaStreams
    lateinit var meterRegistry: MeterRegistry
}

internal val KafkaStreamsPlugin = createApplicationPlugin("KafkaStreamsPlugin", ::KafkaStreamsPluginConfiguration) {
    val kafkaStreams = pluginConfig.kafkaStreams
    val meterRegistry = pluginConfig.meterRegistry

    kafkaStreams.setStateListener { newState, oldState ->
        application.monitor.raise(
            KafkaStreamsStateTransitionEvent,
            KafkaStreamsStateTransition(newState, oldState),
        )
    }

    val kafkaStreamsMetrics = KafkaStreamsMetrics(kafkaStreams).apply {
        bindTo(meterRegistry)
    }

    val started: EventHandler<Application> = { _ ->
        kafkaStreams.cleanUp()
        kafkaStreams.start()
        log.info { "Kafka Streams startet" }
    }
    var stopping: EventHandler<Application> = {}
    stopping = { _ ->
        kafkaStreams.close()
        kafkaStreamsMetrics.close()
        log.info { "Kafka Streams stoppet" }
        application.monitor.unsubscribe(ApplicationStarted, started)
        application.monitor.unsubscribe(ApplicationStopping, stopping)
    }

    on(MonitoringEvent(ApplicationStarted), started)
    on(MonitoringEvent(ApplicationStopping), stopping)
}

internal data class KafkaStreamsStateTransition(val newState: State, val oldState: State)

internal val KafkaStreamsStateTransitionEvent: EventDefinition<KafkaStreamsStateTransition> = EventDefinition()
