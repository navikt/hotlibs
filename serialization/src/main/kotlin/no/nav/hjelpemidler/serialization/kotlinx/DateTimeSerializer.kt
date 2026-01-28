package no.nav.hjelpemidler.serialization.kotlinx

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

object LocalDateSerializer : AbstractStringSerializer<LocalDate>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.LocalDateSerializer",
    toString = LocalDate::toString,
    fromString = LocalDate::parse,
)

object LocalDateTimeSerializer : AbstractStringSerializer<LocalDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.LocalDateTimeSerializer",
    toString = LocalDateTime::toString,
    fromString = LocalDateTime::parse,
)

object OffsetDateTimeSerializer : AbstractStringSerializer<OffsetDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.OffsetDateTimeSerializer",
    toString = OffsetDateTime::toString,
    fromString = OffsetDateTime::parse,
)

object ZonedDateTimeSerializer : AbstractStringSerializer<ZonedDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.ZonedDateTimeSerializer",
    toString = ZonedDateTime::toString,
    fromString = ZonedDateTime::parse,
)

object InstantSerializer : AbstractStringSerializer<Instant>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.InstantSerializer",
    toString = Instant::toString,
    fromString = Instant::parse,
)
