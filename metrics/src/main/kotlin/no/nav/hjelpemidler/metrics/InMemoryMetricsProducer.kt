package no.nav.hjelpemidler.metrics

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

internal class InMemoryMetricsProducer private constructor(
    private val events: Queue<MetricsEvent>,
) : MetricsProducer, Collection<MetricsEvent> by events {
    constructor() : this(ConcurrentLinkedQueue())

    override suspend fun writeEvent(event: MetricsEvent) {
        events.add(event)
    }

    override fun close() {
        events.clear()
    }
}
