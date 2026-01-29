package no.nav.hjelpemidler.serialization.kotlinx

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

object LocalDateSerializer : ToStringSerializer<LocalDate>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.LocalDateSerializer",
    creator = LocalDate::parse,
)

object LocalDateTimeSerializer : ToStringSerializer<LocalDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.LocalDateTimeSerializer",
    creator = LocalDateTime::parse,
)

object OffsetDateTimeSerializer : ToStringSerializer<OffsetDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.OffsetDateTimeSerializer",
    creator = OffsetDateTime::parse,
)

object ZonedDateTimeSerializer : ToStringSerializer<ZonedDateTime>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.ZonedDateTimeSerializer",
    creator = ZonedDateTime::parse,
)

object InstantSerializer : ToStringSerializer<Instant>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.InstantSerializer",
    creator = Instant::parse,
)
