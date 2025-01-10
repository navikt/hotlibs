package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KLoggingEventBuilder
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.oshai.kotlinlogging.Marker

val secureLog: KLogger = object : KLogger {
    private val wrapped = KotlinLogging.logger("tjenestekall")
    private val marker = KMarkerFactory.getMarker("secureLog")

    override val name: String = wrapped.name

    override fun at(level: Level, marker: Marker?, block: KLoggingEventBuilder.() -> Unit) =
        wrapped.at(level, marker ?: this.marker, block)

    override fun isLoggingEnabledFor(level: Level, marker: Marker?): Boolean =
        wrapped.isLoggingEnabledFor(level, marker ?: this.marker)
}
