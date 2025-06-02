package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KLoggingEventBuilder
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.oshai.kotlinlogging.Marker

/**
 * Logger for `tjenestekall` som alltid bruker marker `secureLog` i tilfelle appender skulle kreve det.
 */
@Deprecated("Bruk hotlibs.logging TeamLogsExtensions i stedet.")
val secureLog: KLogger = object : KLogger {
    private val wrapped = KotlinLogging.logger("tjenestekall")
    private val secureLogMarker: Marker = KMarkerFactory.getMarker("secureLog")

    override val name: String = wrapped.name

    override fun at(level: Level, marker: Marker?, block: KLoggingEventBuilder.() -> Unit) =
        wrapped.at(level, secureLogMarker, block)

    override fun isLoggingEnabledFor(level: Level, marker: Marker?): Boolean =
        wrapped.isLoggingEnabledFor(level, secureLogMarker)
}
