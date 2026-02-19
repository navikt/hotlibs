package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.Marker

internal val teamLogsMarker: Marker = KMarkerFactory.getMarker("TEAM_LOGS")

fun KLogger.teamTrace(throwable: Throwable? = null, message: () -> Any?) = trace(teamLogsMarker, throwable, message)
fun KLogger.teamDebug(throwable: Throwable? = null, message: () -> Any?) = debug(teamLogsMarker, throwable, message)
fun KLogger.teamInfo(throwable: Throwable? = null, message: () -> Any?) = info(teamLogsMarker, throwable, message)
fun KLogger.teamWarn(throwable: Throwable? = null, message: () -> Any?) = warn(teamLogsMarker, throwable, message)
fun KLogger.teamError(throwable: Throwable? = null, message: () -> Any?) = error(teamLogsMarker, throwable, message)
