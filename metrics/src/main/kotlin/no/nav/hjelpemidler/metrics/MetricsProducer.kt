package no.nav.hjelpemidler.metrics

import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable
import java.io.Closeable

interface MetricsProducer : Closeable {
    suspend fun writeEvent(event: MetricsEvent)

    suspend fun writeEvent(measurementName: String) =
        writeEvent(MetricsEvent(measurementName))

    suspend fun writeEvent(measurementName: String, fields: Map<String, Any?>) =
        writeEvent(MetricsEvent(measurementName, fields))

    suspend fun writeEvent(measurementName: String, vararg fields: Pair<String, Any?>) =
        writeEvent(MetricsEvent(measurementName, mapOf(*fields)))

    suspend fun writeEvent(measurementName: String, fields: Map<String, Any?>, tags: Map<String, String?>) =
        writeEvent(MetricsEvent(measurementName, fields, tags))

    companion object {
        val DEFAULT_TAGS: Map<String, String> = mapOf(
            "application" to NaisEnvironmentVariable.NAIS_APP_NAME,
            "cluster" to NaisEnvironmentVariable.NAIS_CLUSTER_NAME,
        )
    }
}
