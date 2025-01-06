package no.nav.hjelpemidler.metrics

import no.nav.hjelpemidler.configuration.ClusterEnvironment
import no.nav.hjelpemidler.configuration.Environment
import no.nav.hjelpemidler.metrics.influxdb.InfluxDBMetricsProducer
import no.nav.hjelpemidler.metrics.influxdb.createInfluxDBClientKotlin

fun createMetricsProducer(): MetricsProducer =
    if (Environment.current is ClusterEnvironment) {
        InfluxDBMetricsProducer(createInfluxDBClientKotlin())
    } else {
        InMemoryMetricsProducer()
    }
