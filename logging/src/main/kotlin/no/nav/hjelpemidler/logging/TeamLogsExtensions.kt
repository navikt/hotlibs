package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.Marker

internal val teamLogsMarker: Marker = KMarkerFactory.getMarker("TEAM_LOGS")

fun KLogger.teamTrace(throwable: Throwable? = null, message: () -> Any?) = trace(throwable, teamLogsMarker, message)
fun KLogger.teamDebug(throwable: Throwable? = null, message: () -> Any?) = debug(throwable, teamLogsMarker, message)
fun KLogger.teamInfo(throwable: Throwable? = null, message: () -> Any?) = info(throwable, teamLogsMarker, message)
fun KLogger.teamWarn(throwable: Throwable? = null, message: () -> Any?) = warn(throwable, teamLogsMarker, message)
fun KLogger.teamError(throwable: Throwable? = null, message: () -> Any?) = error(throwable, teamLogsMarker, message)
