package no.nav.hjelpemidler.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory
import io.github.oshai.kotlinlogging.Marker

val secureLogMarker: Marker = KMarkerFactory.getMarker("secureLog")
@Deprecated("Bytt til Team Logs. Se docs: https://docs.nais.io/observability/logging/how-to/team-logs/")
fun KLogger.secureTrace(throwable: Throwable? = null, message: () -> Any?) = trace(throwable, secureLogMarker, message)
@Deprecated("Bytt til Team Logs. Se docs: https://docs.nais.io/observability/logging/how-to/team-logs/")
fun KLogger.secureDebug(throwable: Throwable? = null, message: () -> Any?) = debug(throwable, secureLogMarker, message)
@Deprecated("Bytt til Team Logs. Se docs: https://docs.nais.io/observability/logging/how-to/team-logs/")
fun KLogger.secureInfo(throwable: Throwable? = null, message: () -> Any?) = info(throwable, secureLogMarker, message)
@Deprecated("Bytt til Team Logs. Se docs: https://docs.nais.io/observability/logging/how-to/team-logs/")
fun KLogger.secureWarn(throwable: Throwable? = null, message: () -> Any?) = warn(throwable, secureLogMarker, message)
@Deprecated("Bytt til Team Logs. Se docs: https://docs.nais.io/observability/logging/how-to/team-logs/")
fun KLogger.secureError(throwable: Throwable? = null, message: () -> Any?) = error(throwable, secureLogMarker, message)