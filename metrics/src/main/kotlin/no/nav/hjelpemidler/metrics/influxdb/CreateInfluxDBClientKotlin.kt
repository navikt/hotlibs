package no.nav.hjelpemidler.metrics.influxdb

import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.configuration.InfluxDBEnvironmentVariable

private val log = KotlinLogging.logger {}

internal fun createInfluxDBClientKotlin(): InfluxDBClientKotlin {
    val url = InfluxDBEnvironmentVariable.INFLUX_URL
    log.info { "Oppretter InfluxDBClientKotlin mot url: '$url'" }
    return InfluxDBClientKotlinFactory.create(
        url = url,
        token = "${InfluxDBEnvironmentVariable.INFLUX_USER}:${InfluxDBEnvironmentVariable.INFLUX_PASSWORD}".toCharArray(),
        org = "-",
        bucket = "${InfluxDBEnvironmentVariable.INFLUX_DATABASE_NAME}/",
    )
}
