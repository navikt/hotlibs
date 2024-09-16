package no.nav.hjelpemidler.time

import no.nav.hjelpemidler.localization.LOCALE_NORWEGIAN
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val DATE_TIME_FORMATTER_NORWEGIAN_DATE: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).norwegian()

fun DateTimeFormatter.withLocaleNorwegian(): DateTimeFormatter = withLocale(LOCALE_NORWEGIAN)
fun DateTimeFormatter.withZoneEuropeOslo(): DateTimeFormatter = withZone(ZONE_ID_EUROPE_OSLO)
fun DateTimeFormatter.norwegian(): DateTimeFormatter = withLocaleNorwegian().withZoneEuropeOslo()
