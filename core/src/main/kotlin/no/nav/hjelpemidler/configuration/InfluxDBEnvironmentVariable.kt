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
    val INFLUX_URL by EnvironmentVariable

    @External
    val INFLUX_USER by EnvironmentVariable

    @External
    val INFLUX_PASSWORD by EnvironmentVariable

    /**
     * Bucket.
     */
    @External
    val INFLUX_DATABASE_NAME by EnvironmentVariable

    /**
     * NB! Denne er definert med 'https://' i secret.
     */
    @External
    val INFLUX_HOST by EnvironmentVariable

    @External
    val INFLUX_PORT by EnvironmentVariable
}
