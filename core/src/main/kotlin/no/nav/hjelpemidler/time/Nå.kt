package no.nav.hjelpemidler.time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun nå(): Instant = Instant.now()
fun nåtz(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): ZonedDateTime = ZonedDateTime.now(zoneId)
fun iDag(): LocalDate = LocalDate.now()
fun dagsDato(): LocalDate = LocalDate.now()
