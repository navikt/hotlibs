package no.nav.hjelpemidler.metrics.influxdb

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.WriteKotlinApi
import com.influxdb.client.write.Point
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.metrics.MetricsEvent
import no.nav.hjelpemidler.metrics.MetricsProducer
import java.io.Closeable
import java.time.Instant

private val log = KotlinLogging.logger {}

internal class InfluxDBMetricsProducer(client: InfluxDBClientKotlin) : MetricsProducer, Closeable by client {
    private val writeApi: WriteKotlinApi = client.getWriteKotlinApi()

    override suspend fun writeEvent(event: MetricsEvent) {
        try {
            writeApi.writePoint(
                Point
                    .measurement(event.measurementName)
                    .addTags(MetricsProducer.DEFAULT_TAGS)
                    .addTags(event.tags)
                    .addFields(event.fields)
                    .time(Instant.now(), WritePrecision.MS)
            )
        } catch (e: Exception) {
            log.warn(e) { "Feil under overføring av metrikk til InfluxDB" }
        }
    }

    override val history: Collection<MetricsEvent>
        get() = throw UnsupportedOperationException("InfluxDBMetricsProducer tar ikke vare på hendelser")
}
