package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Marker

val secureLog: KLogger = KotlinLogging.logger("tjenestekall")

val secureLogMarker: Marker = KMarkerFactory.getMarker("secureLog")
fun KLogger.secureTrace(throwable: Throwable? = null, message: () -> Any?) = trace(throwable, secureLogMarker, message)
fun KLogger.secureDebug(throwable: Throwable? = null, message: () -> Any?) = debug(throwable, secureLogMarker, message)
fun KLogger.secureInfo(throwable: Throwable? = null, message: () -> Any?) = info(throwable, secureLogMarker, message)
fun KLogger.secureWarn(throwable: Throwable? = null, message: () -> Any?) = warn(throwable, secureLogMarker, message)
fun KLogger.secureError(throwable: Throwable? = null, message: () -> Any?) = error(throwable, secureLogMarker, message)
