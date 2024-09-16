package no.nav.hjelpemidler.configuration

/**
 * NB! Krever følgende i nais.yaml:
 * ```
 *   envFrom:
 *     - secret: hm-influxdb-secret
 * ```
 */
object InfluxDBEnvironmentVariable {
    @External
    val INFLUX_HOST by EnvironmentVariable

    @External
    val INFLUX_PORT by EnvironmentVariable

    @External
    val INFLUX_DATABASE_NAME by EnvironmentVariable

    @External
    val INFLUX_USER by EnvironmentVariable

    @External
    val INFLUX_PASSWORD by EnvironmentVariable
}
